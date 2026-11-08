package io.perklens.data.sync

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import io.perklens.core.diagnostics.AppDiagnosticsLogManager
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

class RemoteCatalogSyncManager(
    private val context: Context,
    private val diagnosticsLogger: AppDiagnosticsLogManager? = null
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("catalog_sync_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val cachedFile = File(context.filesDir, "cards_catalog_cached.json")

    private val KEY_LAST_SYNC_TIME = "last_sync_time"
    private val KEY_CUSTOM_URL = "custom_sync_url"

    // Default primary endpoint and resilient CDN mirrors
    val DEFAULT_SYNC_URL = "https://raw.githubusercontent.com/sakinator/PerkLens/main/app/src/main/assets/cards_catalog.json"
    val CDN_MIRROR_URL = "https://cdn.jsdelivr.net/gh/sakinator/PerkLens@main/app/src/main/assets/cards_catalog.json"
    val FALLBACK_RAW_URL = "https://raw.githubusercontent.com/sakinator/PerkLens/main/assets/cards_catalog.json"

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    private val client = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(18, TimeUnit.SECONDS)
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

        val mirrorsToTry = if (!forcedUrl.isNullOrBlank()) {
            listOf(forcedUrl)
        } else {
            listOf(getSyncUrl(), CDN_MIRROR_URL, FALLBACK_RAW_URL).distinct()
        }

        var lastError: String? = null
        var lastThrowable: Throwable? = null

        for (targetUrl in mirrorsToTry) {
            try {
                val request = Request.Builder()
                    .url(targetUrl)
                    .header("User-Agent", "PerkLens-Android/1.0 (Linux; Android 14)")
                    .header("Cache-Control", "no-cache")
                    .build()

                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    val errMsg = "HTTP ${response.code} from $targetUrl"
                    diagnosticsLogger?.logWarning("CATALOG_SYNC", errMsg)
                    lastError = errMsg
                    continue
                }

                val bodyString = response.body?.string() ?: ""
                if (bodyString.isBlank()) {
                    val errMsg = "Empty response payload from $targetUrl"
                    diagnosticsLogger?.logWarning("CATALOG_SYNC", errMsg)
                    lastError = errMsg
                    continue
                }

                val parsed = gson.fromJson(bodyString, CardCatalogContainer::class.java)
                if (parsed.cards.isEmpty() && parsed.liveOffers.isEmpty()) {
                    val errMsg = "Downloaded catalog failed schema validation (zero cards)"
                    diagnosticsLogger?.logWarning("CATALOG_SYNC", errMsg)
                    lastError = errMsg
                    continue
                }

                // Write to local cache file
                cachedFile.writeText(bodyString)

                val nowStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date())
                prefs.edit().putString(KEY_LAST_SYNC_TIME, nowStr).apply()

                val successMsg = "Updated ${parsed.cards.size} cards & ${parsed.liveOffers.size} offers"
                _syncState.value = SyncState.Success(
                    message = successMsg,
                    timestamp = nowStr
                )
                diagnosticsLogger?.logInfo("CATALOG_SYNC", "Catalog synced successfully ($successMsg) from $targetUrl")
                return@withContext true
            } catch (e: Exception) {
                lastError = e.localizedMessage ?: "Connection error"
                lastThrowable = e
                diagnosticsLogger?.logWarning("CATALOG_SYNC", "Failed mirror $targetUrl: ${e.message}")
            }
        }

        // If all mirrors failed
        val finalErrMsg = lastError ?: "All catalog update sources failed"
        _syncState.value = SyncState.Error(finalErrMsg)
        diagnosticsLogger?.logError("CATALOG_SYNC", "Catalog sync completely failed across ${mirrorsToTry.size} sources: $finalErrMsg", lastThrowable)
        return@withContext false
    }
}
