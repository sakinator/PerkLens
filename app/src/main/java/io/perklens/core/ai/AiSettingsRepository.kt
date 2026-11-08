package io.perklens.core.ai

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AiProvider(val displayName: String, val badge: String) {
    GEMINI_FREE("Google Gemini (Free Public Tier)", "Zero Key Required"),
    GEMINI_KEY("Google Gemini (Custom API Key)", "Gemini 1.5/2.0 Flash"),
    GROQ("Groq (Ultra-Fast Llama 3)", "High Speed"),
    OPENAI("OpenAI (GPT-4o Mini)", "Custom Key"),
    CLAUDE("Anthropic Claude 3.5 Sonnet", "Custom Key"),
    OFFLINE_SMART("On-Device Smart Heuristics", "100% Offline")
}

class AiSettingsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("perklens_ai_settings", Context.MODE_PRIVATE)

    private val KEY_PROVIDER = "ai_provider"
    private val KEY_GEMINI_KEY = "key_gemini"
    private val KEY_OPENAI_KEY = "key_openai"
    private val KEY_CLAUDE_KEY = "key_claude"
    private val KEY_GROQ_KEY = "key_groq"
    private val KEY_SUGGEST_LTF_ONLY = "suggest_ltf_only"

    private val _suggestLtfOnly = MutableStateFlow(prefs.getBoolean(KEY_SUGGEST_LTF_ONLY, false))
    val suggestLtfOnly: StateFlow<Boolean> = _suggestLtfOnly.asStateFlow()

    private val _selectedProvider = MutableStateFlow(loadProvider())
    val selectedProvider: StateFlow<AiProvider> = _selectedProvider.asStateFlow()

    private fun loadProvider(): AiProvider {
        val name = prefs.getString(KEY_PROVIDER, AiProvider.GEMINI_FREE.name)
        return try {
            AiProvider.valueOf(name ?: AiProvider.GEMINI_FREE.name)
        } catch (e: Exception) {
            AiProvider.GEMINI_FREE
        }
    }

    fun setProvider(provider: AiProvider) {
        prefs.edit().putString(KEY_PROVIDER, provider.name).apply()
        _selectedProvider.value = provider
    }

    fun setSuggestLtfOnly(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SUGGEST_LTF_ONLY, enabled).apply()
        _suggestLtfOnly.value = enabled
    }

    fun getApiKey(provider: AiProvider): String {
        return when (provider) {
            AiProvider.GEMINI_KEY -> prefs.getString(KEY_GEMINI_KEY, "") ?: ""
            AiProvider.OPENAI -> prefs.getString(KEY_OPENAI_KEY, "") ?: ""
            AiProvider.CLAUDE -> prefs.getString(KEY_CLAUDE_KEY, "") ?: ""
            AiProvider.GROQ -> prefs.getString(KEY_GROQ_KEY, "") ?: ""
            else -> ""
        }
    }

    fun setApiKey(provider: AiProvider, key: String) {
        val prefKey = when (provider) {
            AiProvider.GEMINI_KEY -> KEY_GEMINI_KEY
            AiProvider.OPENAI -> KEY_OPENAI_KEY
            AiProvider.CLAUDE -> KEY_CLAUDE_KEY
            AiProvider.GROQ -> KEY_GROQ_KEY
            else -> null
        }
        prefKey?.let {
            prefs.edit().putString(it, key.trim()).apply()
        }
    }
}
