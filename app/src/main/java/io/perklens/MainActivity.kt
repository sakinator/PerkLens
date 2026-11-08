package io.perklens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import io.perklens.core.ai.AiSettingsRepository
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val syncManager = RemoteCatalogSyncManager(applicationContext)
        val cardRepository = CardRepository(applicationContext, syncManager)
        val walletRepository = UserWalletRepository(applicationContext)
        val loungeRepository = LoungeRepository(applicationContext)
        val categoryOffersRepository = CategoryOffersRepository(cardRepository)
        val aiSettings = AiSettingsRepository(applicationContext)
        val smsSyncManager = SmsSyncManager(applicationContext, walletRepository, cardRepository)
        val gmailSyncManager = GmailSyncManager(applicationContext)

        // Self-Sufficient Background Sync: Check for new offers & cards on launch without blocking UI
        lifecycleScope.launch(Dispatchers.IO) {
            val updated = syncManager.syncLatestCatalog()
            if (updated) {
                cardRepository.reloadCatalog()
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
                        gmailSyncManager = gmailSyncManager
                    )
                }
            }
        }
    }
}
