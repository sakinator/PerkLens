package io.perklens.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.perklens.core.ai.AiSettingsRepository
import io.perklens.core.ai.CardRecommendation
import io.perklens.core.ai.PortfolioRecommendationEngine
import io.perklens.data.model.Card
import io.perklens.data.repository.CardRepository
import io.perklens.data.repository.UserWalletRepository
import io.perklens.ui.components.CardDetailBottomSheet
import io.perklens.ui.components.CreditCardVisual
import io.perklens.ui.theme.*

@Composable
fun AiAdvisorScreen(
    cardRepo: CardRepository,
    walletRepo: UserWalletRepository,
    aiSettings: AiSettingsRepository,
    onNavigateToSettings: () -> Unit
) {
    val savedCardIds by walletRepo.savedCardIds.collectAsState()
    val allCards = remember { cardRepo.getAllCards() }
    val userCards = remember(savedCardIds) {
        allCards.filter { savedCardIds.contains(it.id) }
    }

    val suggestLtfOnly by aiSettings.suggestLtfOnly.collectAsState()

    val coverage = remember(userCards) {
        PortfolioRecommendationEngine.analyzeCoverage(userCards)
    }

    val recommendations = remember(userCards, allCards, suggestLtfOnly) {
        PortfolioRecommendationEngine.recommendNewCards(userCards, allCards, suggestLtfOnly)
    }

    var selectedCardForDetail by remember { mutableStateOf<Card?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title Row with Settings Shortcut
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "AI Portfolio Advisor",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Portfolio gap analysis & personalized card recommendations",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            IconButton(onClick = onNavigateToSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "AI & Sync Settings",
                    tint = EmeraldGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Portfolio Health Gauge Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkSurface)
                        .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Wallet Optimization Score",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${coverage.healthScorePercent}% Optimized",
                                color = if (coverage.healthScorePercent >= 70) EmeraldGreen else AmberGold,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (coverage.healthScorePercent >= 70) EmeraldGreen.copy(alpha = 0.2f)
                                    else AmberGold.copy(alpha = 0.2f)
                                )
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = if (coverage.healthScorePercent >= 70) EmeraldGreen else AmberGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = coverage.healthScorePercent / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (coverage.healthScorePercent >= 70) EmeraldGreen else AmberGold,
                        trackColor = DarkSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Coverage Badges Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CoveragePill(label = "RuPay UPI", covered = coverage.hasRupayUpi, modifier = Modifier.weight(1f))
                        CoveragePill(label = "Lounge", covered = coverage.hasLoungeAccess, modifier = Modifier.weight(1f))
                        CoveragePill(label = "Dining 10%", covered = coverage.hasDiningAccelerator, modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CoveragePill(label = "5% Online", covered = coverage.hasOnlineCashback, modifier = Modifier.weight(1f))
                        CoveragePill(label = "Low Forex", covered = coverage.hasZeroForex, modifier = Modifier.weight(1f))
                    }
                }
            }

            // Setting Toggle: Suggest LTF Cards Only
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceVariant)
                        .border(1.dp, if (suggestLtfOnly) EmeraldGreen else DarkBorder, RoundedCornerShape(12.dp))
                        .clickable { aiSettings.setSuggestLtfOnly(!suggestLtfOnly) }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Suggest Lifetime Free (LTF) Only",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (suggestLtfOnly) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(EmeraldGreen)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("ACTIVE", color = DarkBackground, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Text(
                            text = "Only recommend cards with zero joining & annual fee forever",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Switch(
                        checked = suggestLtfOnly,
                        onCheckedChange = { aiSettings.setSuggestLtfOnly(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = DarkBackground,
                            checkedTrackColor = EmeraldGreen,
                            uncheckedTrackColor = DarkBorder
                        )
                    )
                }
            }

            // Recommended Cards Section
            item {
                Text(
                    text = if (suggestLtfOnly) "Recommended LTF Cards to Fill Gaps" else "Top AI Recommended Cards for Your Wallet",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(recommendations) { rec ->
                RecommendationCardItem(
                    rec = rec,
                    onCardClick = { selectedCardForDetail = rec.card },
                    onAddCard = { walletRepo.addCard(rec.card.id) }
                )
            }
        }
    }

    // Detail BottomSheet
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
fun CoveragePill(label: String, covered: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (covered) EmeraldGreen.copy(alpha = 0.15f) else DarkSurfaceVariant)
            .border(0.5.dp, if (covered) EmeraldGreen else DarkBorder, RoundedCornerShape(8.dp))
            .padding(vertical = 6.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (covered) "✓ $label" else "✗ $label",
            color = if (covered) EmeraldGreen else TextTertiary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RecommendationCardItem(
    rec: CardRecommendation,
    onCardClick: () -> Unit,
    onAddCard: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
            .clickable { onCardClick() }
            .padding(16.dp)
    ) {
        // High-res credit card visual
        CreditCardVisual(card = rec.card, modifier = Modifier.padding(bottom = 12.dp))

        // Headline AI Reason
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ElectricCyan.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "SOLVES: ${rec.solvedCategoryGap.uppercase()}",
                    color = ElectricCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "+₹${rec.estimatedAnnualBenefitInr}/yr",
                color = EmeraldGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = rec.headlineReason,
            color = TextPrimary,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (rec.isLtf) "Lifetime Free (LTF)" else "Annual Fee: ₹${rec.card.annualFee}",
                color = if (rec.isLtf) EmeraldGreen else AmberGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onAddCard,
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldGreen,
                    contentColor = DarkBackground
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add to Vault", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
