package io.perklens.data.sync

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import io.perklens.data.model.CardCatalogContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

sealed class SyncState {
    object Idle : SyncState()
    object Syncing : SyncState()
    data class Success(val message: String, val timestamp: String) : SyncState()
    data class Error(val error: String) : SyncState()
}

class RemoteCatalogSyncManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("catalog_sync_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val cachedFile = File(context.filesDir, "cards_catalog_cached.json")

    private val KEY_LAST_SYNC_TIME = "last_sync_time"
    private val KEY_CUSTOM_URL = "custom_sync_url"

    // Default public endpoint (can be any raw GitHub / jsDelivr / Cloudflare URL)
    val DEFAULT_SYNC_URL = "https://raw.githubusercontent.com/sakinator/PerkLens/main/app/src/main/assets/cards_catalog.json"

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    fun getLastSyncTime(): String {
        return prefs.getString(KEY_LAST_SYNC_TIME, "Never") ?: "Never"
    }

    fun getSyncUrl(): String {
        return prefs.getString(KEY_CUSTOM_URL, DEFAULT_SYNC_URL) ?: DEFAULT_SYNC_URL
    }

    fun setSyncUrl(url: String) {
        prefs.edit().putString(KEY_CUSTOM_URL, url.trim()).apply()
    }

    fun getCachedCatalogFile(): File? {
        return if (cachedFile.exists() && cachedFile.length() > 0) cachedFile else null
    }

    suspend fun syncLatestCatalog(forcedUrl: String? = null): Boolean = withContext(Dispatchers.IO) {
        _syncState.value = SyncState.Syncing
        val url = forcedUrl ?: getSyncUrl()

        try {
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "PerkLens-Android/1.0")
                .header("Cache-Control", "no-cache")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                _syncState.value = SyncState.Error("Server returned code ${response.code}")
                return@withContext false
            }

            val bodyString = response.body?.string() ?: ""
            if (bodyString.isBlank()) {
                _syncState.value = SyncState.Error("Empty response from server")
                return@withContext false
            }

            // Validate that downloaded string is valid JSON matching our schema
            val parsed = gson.fromJson(bodyString, CardCatalogContainer::class.java)
            if (parsed.cards.isEmpty() && parsed.liveOffers.isEmpty()) {
                _syncState.value = SyncState.Error("Invalid catalog schema")
                return@withContext false
            }

            // Write to local cache file
            cachedFile.writeText(bodyString)

            val nowStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date())
            prefs.edit().putString(KEY_LAST_SYNC_TIME, nowStr).apply()

            _syncState.value = SyncState.Success(
                message = "Updated ${parsed.cards.size} cards & ${parsed.liveOffers.size} offers",
                timestamp = nowStr
            )
            true
        } catch (e: Exception) {
            _syncState.value = SyncState.Error(e.localizedMessage ?: "Sync connection failed")
            false
        }
    }
}
