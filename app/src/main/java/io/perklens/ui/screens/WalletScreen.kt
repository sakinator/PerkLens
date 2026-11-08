package io.perklens.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.perklens.core.ai.AiSettingsRepository
import io.perklens.data.model.Card
import io.perklens.data.repository.CardRepository
import io.perklens.data.repository.CategoryOffersRepository
import io.perklens.data.repository.UserWalletRepository
import io.perklens.ui.components.CameraCardScanSheet
import io.perklens.ui.components.CardDetailBottomSheet
import io.perklens.ui.components.CreditCardVisual
import io.perklens.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    cardRepo: CardRepository,
    walletRepo: UserWalletRepository,
    aiSettings: AiSettingsRepository,
    categoryOffersRepo: CategoryOffersRepository? = null,
    onNavigateToSettings: () -> Unit = {}
) {
    val context = LocalContext.current
    val savedCardIds by walletRepo.savedCardIds.collectAsState()
    val cardUsages by walletRepo.cardUsages.collectAsState()
    val allCards = remember { cardRepo.getAllCards() }
    val userCards = remember(savedCardIds) {
        allCards.filter { savedCardIds.contains(it.id) }
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var showCameraScanSheet by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCardForDetail by remember { mutableStateOf<Card?>(null) }

    Scaffold(
        containerColor = DarkBackground,
        floatingActionButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Camera Multi-Card Scan FAB
                FloatingActionButton(
                    onClick = { showCameraScanSheet = true },
                    containerColor = ElectricCyan,
                    contentColor = DarkBackground
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Scan Cards with Camera")
                }

                // Add by Name FAB
                ExtendedFloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = EmeraldGreen,
                    contentColor = DarkBackground,
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add Card") },
                    text = { Text("Add Card", fontWeight = FontWeight.Bold) }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header Banner
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Card Vault",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${userCards.size} cards • Tap card for Perks & Quota Tracker",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { showCameraScanSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "AI Camera Multi-Card Scan",
                            tint = ElectricCyan
                        )
                    }

                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "AI & Catalog Settings",
                            tint = NeonGreen
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(EmeraldGreen.copy(alpha = 0.15f))
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Zero KYC Secure",
                            tint = EmeraldGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick AI & Catalog Status Banner
            val currentAiProvider by aiSettings.selectedProvider.collectAsState()
            val aiName = remember(currentAiProvider) { currentAiProvider.displayName.substringBefore('(').trim() }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceVariant)
                    .border(1.dp, DarkBorderNeon, RoundedCornerShape(12.dp))
                    .clickable { onNavigateToSettings() }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NeonGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "AI: $aiName",
                        color = NeonGreenBright,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${allCards.size} Cards Synced ✓",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Configure",
                        tint = TextTertiary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (userCards.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No cards added yet",
                            color = TextSecondary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap '📷' to scan multiple cards or '+ Add Card' by name",
                            color = TextTertiary,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(userCards, key = { it.id }) { card ->
                        val usage = cardUsages[card.id]
                        val billingDay = usage?.billingCycleDay ?: 15
                        val graceDays = walletRepo.calculateGracePeriodDays(billingDay)

                        CardItemWithPerks(
                            card = card,
                            graceDays = graceDays,
                            billingDay = billingDay,
                            moviesUsed = usage?.availedMovieTicketsMonth ?: 0,
                            onClick = { selectedCardForDetail = card },
                            onRemove = { walletRepo.removeCard(card.id) }
                        )
                    }
                }
            }
        }
    }

    // Detail BottomSheet
    if (selectedCardForDetail != null) {
        CardDetailBottomSheet(
            card = selectedCardForDetail!!,
            userCards = userCards,
            aiSettings = aiSettings,
            categoryOffersRepo = categoryOffersRepo,
            walletRepo = walletRepo,
            onDismiss = { selectedCardForDetail = null }
        )
    }

    // Camera Multi-Card Scan Sheet
    if (showCameraScanSheet) {
        CameraCardScanSheet(
            cardRepo = cardRepo,
            walletRepo = walletRepo,
            onDismiss = { showCameraScanSheet = false },
            onCardsAdded = { count ->
                Toast.makeText(context, "Added $count cards to your Vault!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Add Card Modal BottomSheet
    if (showAddDialog) {
        ModalBottomSheet(
            onDismissRequest = {
                showAddDialog = false
                searchQuery = ""
            },
            containerColor = DarkSurface,
            dragHandle = { BottomSheetDefaults.DragHandle(color = DarkBorder) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Add Card by Name",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "No card number or OTP required. Simply search and pick.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search e.g. Millennia, SBI Cashback, Airtel...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = TextSecondary)
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = EmeraldGreen,
                        unfocusedBorderColor = DarkBorder,
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                val filteredCards = remember(searchQuery) {
                    cardRepo.searchCards(searchQuery)
                }

                LazyColumn(
                    modifier = Modifier.heightIn(max = 420.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredCards) { card ->
                        val isAdded = savedCardIds.contains(card.id)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkSurfaceVariant)
                                .clickable {
                                    if (isAdded) {
                                        walletRepo.removeCard(card.id)
                                    } else {
                                        walletRepo.addCard(card.id)
                                    }
                                }
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = card.name,
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${card.issuer} • ${card.network}",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = if (card.isLifetimeFree) "Lifetime Free" else "Annual Fee: ₹${card.annualFee}",
                                    color = if (card.isLifetimeFree) EmeraldGreen else AmberGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Button(
                                onClick = {
                                    if (isAdded) walletRepo.removeCard(card.id) else walletRepo.addCard(card.id)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isAdded) DarkBorder else EmeraldGreen,
                                    contentColor = if (isAdded) TextSecondary else DarkBackground
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                if (isAdded) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Added", fontSize = 12.sp)
                                } else {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CardItemWithPerks(
    card: Card,
    graceDays: Int,
    billingDay: Int,
    moviesUsed: Int,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorderNeon, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        // Visual Card Canvas
        CreditCardVisual(card = card)

        Spacer(modifier = Modifier.height(10.dp))

        // Perks & Runway Metrics Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Interest-Free Runway Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NeonGreen.copy(alpha = 0.15f))
                        .border(0.5.dp, NeonGreen.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "⚡ $graceDays Days Runway",
                        color = NeonGreenBright,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Billing Day Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DarkSurfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "📅 Bill Day $billingDay",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Cinema Quota Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(OfferBadgeGold.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "🎬 $moviesUsed/2 BOGO",
                        color = OfferBadgeGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Remove Button
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove Card",
                    tint = TextTertiary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
