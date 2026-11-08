package io.perklens.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.perklens.core.ai.AiSettingsRepository
import io.perklens.data.model.Card
import io.perklens.data.repository.CardRepository
import io.perklens.data.repository.UserWalletRepository
import io.perklens.ui.components.CardDetailBottomSheet
import io.perklens.ui.components.CreditCardVisual
import io.perklens.ui.theme.*

@Composable
fun DiscoverScreen(
    cardRepo: CardRepository,
    walletRepo: UserWalletRepository,
    aiSettings: AiSettingsRepository
) {
    val savedCardIds by walletRepo.savedCardIds.collectAsState()
    val allCards = remember { cardRepo.getAllCards() }
    val userCards = remember(savedCardIds) {
        allCards.filter { savedCardIds.contains(it.id) }
    }

    var selectedFilter by remember { mutableStateOf("All") }
    var selectedCardForDetail by remember { mutableStateOf<Card?>(null) }

    val filterOptions = listOf(
        "All" to "All Market Cards",
        "LTF" to "Lifetime Free (LTF)",
        "Cashback" to "Cashback Champions",
        "Travel" to "Travel & Forex",
        "RuPay" to "RuPay UPI Cards",
        "LowFee" to "Low Annual Fee (<₹1000)"
    )

    val displayedCards = remember(selectedFilter, allCards) {
        cardRepo.getCardsByFilter(selectedFilter)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Market Discovery",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Explore cards in market with authentic art, fees & AI verdicts",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = Icons.Default.Explore,
                contentDescription = null,
                tint = ElectricCyan,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filters Horizontal Scroll
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterOptions.forEach { (key, label) ->
                val isSelected = selectedFilter == key
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = key },
                    label = { Text(label, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricCyan.copy(alpha = 0.2f),
                        selectedLabelColor = ElectricCyan,
                        containerColor = DarkSurfaceVariant,
                        labelColor = TextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) ElectricCyan else DarkBorder,
                        selectedBorderColor = ElectricCyan
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(displayedCards) { card ->
                val isSaved = savedCardIds.contains(card.id)
                MarketCardItem(
                    card = card,
                    isSaved = isSaved,
                    onClick = { selectedCardForDetail = card },
                    onToggleSave = {
                        if (isSaved) walletRepo.removeCard(card.id) else walletRepo.addCard(card.id)
                    }
                )
            }
        }
    }

    if (selectedCardForDetail != null) {
        CardDetailBottomSheet(
            card = selectedCardForDetail!!,
            userCards = userCards,
            aiSettings = aiSettings,
            onDismiss = { selectedCardForDetail = null }
        )
    }
}

@Composable
fun MarketCardItem(
    card: Card,
    isSaved: Boolean,
    onClick: () -> Unit,
    onToggleSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        // High-res visual card view
        CreditCardVisual(card = card, modifier = Modifier.padding(bottom = 14.dp))

        // Header & Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                if (!card.bankLogoUrl.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        AsyncImage(
                            model = card.bankLogoUrl,
                            contentDescription = card.issuer,
                            modifier = Modifier.fillMaxHeight(),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Column {
                    Text(
                        text = card.name,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${card.issuer} • ${card.network}",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(EmeraldGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Text("AI Verdict", color = EmeraldGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(6.dp))

                Button(
                    onClick = onToggleSave,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaved) DarkSurfaceVariant else EmeraldGreen,
                        contentColor = if (isSaved) EmeraldGreen else DarkBackground
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    if (isSaved) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("In Vault", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Fee Badges
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DarkSurfaceVariant)
                    .padding(8.dp)
            ) {
                Text(text = "Joining Fee", color = TextTertiary, fontSize = 10.sp)
                Text(
                    text = if (card.joiningFee == 0) "FREE" else "₹${card.joiningFee}",
                    color = if (card.joiningFee == 0) EmeraldGreen else TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DarkSurfaceVariant)
                    .padding(8.dp)
            ) {
                Text(text = "Annual Fee", color = TextTertiary, fontSize = 10.sp)
                Text(
                    text = if (card.isLifetimeFree) "LIFETIME FREE" else "₹${card.annualFee}",
                    color = if (card.isLifetimeFree) EmeraldGreen else AmberGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DarkSurfaceVariant)
                    .padding(8.dp)
            ) {
                Text(text = "Fee Waiver", color = TextTertiary, fontSize = 10.sp)
                Text(
                    text = if (card.isLifetimeFree) "N/A" else "₹${card.waiverSpendThreshold / 1000}k spend",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "✈️ ${card.loungeAccess}",
                color = TextSecondary,
                fontSize = 11.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Forex: ${card.forexMarkupPercent}%",
                color = if (card.forexMarkupPercent <= 2.0) EmeraldGreen else TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "🎯 Best For: ${card.bestForCategory}",
            color = ElectricCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
