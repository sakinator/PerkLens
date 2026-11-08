package io.perklens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import io.perklens.core.ai.AiSettingsRepository
import io.perklens.core.diagnostics.AppDiagnosticsLogManager
import io.perklens.core.parser.SmsSyncManager
import io.perklens.core.sync.GmailSyncManager
import io.perklens.data.repository.CardRepository
import io.perklens.data.repository.CategoryOffersRepository
import io.perklens.data.repository.LoungeRepository
import io.perklens.data.repository.UserWalletRepository
import io.perklens.data.sync.RemoteCatalogSyncManager
import io.perklens.ui.MainScreen
import io.perklens.ui.theme.PerkLensTheme
import io.perklens.ui.theme.DarkBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val diagnosticsLogger = AppDiagnosticsLogManager(applicationContext)
        val syncManager = RemoteCatalogSyncManager(applicationContext, diagnosticsLogger)
        val cardRepository = CardRepository(applicationContext, syncManager)
        val walletRepository = UserWalletRepository(applicationContext)
        val loungeRepository = LoungeRepository(applicationContext)
        val categoryOffersRepository = CategoryOffersRepository(cardRepository)
        val aiSettings = AiSettingsRepository(applicationContext)
        val smsSyncManager = SmsSyncManager(applicationContext, walletRepository, cardRepository)
        val gmailSyncManager = GmailSyncManager(applicationContext)
        val liveWebDiscoveryManager = io.perklens.core.ai.LiveWebCardDiscoveryManager(applicationContext, diagnosticsLogger)

        // As soon as the app launches look for all updates on cards new cards from all sources
        // In background: query remote catalog mirrors + live network feeds (RuPay, Visa, Mastercard, Bank feeds)
        // Once update is complete, show a toast notification "cards and offers database updated"
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val mirrorUpdated = syncManager.syncLatestCatalog()
                val liveInternetUpdated = liveWebDiscoveryManager.syncLiveInternetUpdatesOnStartup(cardRepository, aiSettings)
                if (mirrorUpdated || liveInternetUpdated) {
                    cardRepository.reloadCatalog()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "cards and offers database updated", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                diagnosticsLogger.logError("APP_LAUNCH", "Error during startup catalog sync: ${e.message}", e)
            }
        }

        setContent {
            PerkLensTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    MainScreen(
                        cardRepo = cardRepository,
                        walletRepo = walletRepository,
                        aiSettings = aiSettings,
                        syncManager = syncManager,
                        loungeRepo = loungeRepository,
                        categoryOffersRepo = categoryOffersRepository,
                        smsSyncManager = smsSyncManager,
                        gmailSyncManager = gmailSyncManager,
                        diagnosticsLogger = diagnosticsLogger,
                        liveWebDiscoveryManager = liveWebDiscoveryManager
                    )
                }
            }
        }
    }
}
