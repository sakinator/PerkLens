package io.perklens.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.perklens.core.ai.AiSettingsRepository
import io.perklens.core.parser.SmsSyncManager
import io.perklens.core.sync.GmailSyncManager
import io.perklens.data.repository.CardRepository
import io.perklens.data.repository.CategoryOffersRepository
import io.perklens.data.repository.LoungeRepository
import io.perklens.data.repository.UserWalletRepository
import io.perklens.data.sync.RemoteCatalogSyncManager
import io.perklens.ui.screens.*
import io.perklens.ui.theme.*

sealed class NavItem(val route: String, val title: String, val icon: ImageVector) {
    object Wallet : NavItem("wallet", "My Vault", Icons.Default.CreditCard)
    object Offers : NavItem("offers", "Offers", Icons.Default.Movie)
    object Lounges : NavItem("lounges", "Lounges", Icons.Default.FlightTakeoff)
    object Decide : NavItem("decide", "Best Swipe", Icons.Default.Savings)
    object Discover : NavItem("discover", "Discover", Icons.Default.Explore)
    object Advisor : NavItem("advisor", "AI Advisor", Icons.Default.AutoAwesome)
}

@Composable
fun MainScreen(
    cardRepo: CardRepository,
    walletRepo: UserWalletRepository,
    aiSettings: AiSettingsRepository,
    syncManager: RemoteCatalogSyncManager,
    loungeRepo: LoungeRepository,
    categoryOffersRepo: CategoryOffersRepository,
    smsSyncManager: SmsSyncManager? = null,
    gmailSyncManager: GmailSyncManager? = null
) {
    var selectedTab by remember { mutableStateOf<NavItem>(NavItem.Wallet) }
    var showSettingsScreen by remember { mutableStateOf(false) }

    val items = listOf(
        NavItem.Wallet,
        NavItem.Offers,
        NavItem.Lounges,
        NavItem.Decide,
        NavItem.Discover,
        NavItem.Advisor
    )

    if (showSettingsScreen) {
        SettingsScreen(
            aiSettings = aiSettings,
            syncManager = syncManager,
            cardRepo = cardRepo,
            walletRepo = walletRepo,
            smsSyncManager = smsSyncManager,
            gmailSyncManager = gmailSyncManager,
            onBack = { showSettingsScreen = false }
        )
    } else {
        Scaffold(
            containerColor = DarkBackground,
            bottomBar = {
                NavigationBar(
                    containerColor = DarkSurface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            listOf(DarkBorderNeon, Color.Transparent)
                        )
                    )
                ) {
                    items.forEach { item ->
                        val isSelected = selectedTab == item
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedTab = item },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EmeraldGreen,
                                selectedTextColor = EmeraldGreen,
                                unselectedIconColor = TextTertiary,
                                unselectedTextColor = TextTertiary,
                                indicatorColor = EmeraldGreen.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier.padding(innerPadding),
                color = DarkBackground
            ) {
                when (selectedTab) {
                    is NavItem.Wallet -> WalletScreen(
                        cardRepo = cardRepo,
                        walletRepo = walletRepo,
                        aiSettings = aiSettings,
                        categoryOffersRepo = categoryOffersRepo
                    )
                    is NavItem.Offers -> CategoryOffersScreen(
                        categoryOffersRepo = categoryOffersRepo,
                        walletRepo = walletRepo,
                        cardRepo = cardRepo
                    )
                    is NavItem.Lounges -> LoungeScreen(
                        loungeRepo = loungeRepo,
                        cardRepo = cardRepo,
                        walletRepo = walletRepo
                    )
                    is NavItem.Decide -> DecisionScreen(
                        cardRepo = cardRepo,
                        walletRepo = walletRepo
                    )
                    is NavItem.Discover -> DiscoverScreen(
                        cardRepo = cardRepo,
                        walletRepo = walletRepo,
                        aiSettings = aiSettings
                    )
                    is NavItem.Advisor -> AiAdvisorScreen(
                        cardRepo = cardRepo,
                        walletRepo = walletRepo,
                        aiSettings = aiSettings,
                        onNavigateToSettings = { showSettingsScreen = true }
                    )
                }
            }
        }
    }
}
