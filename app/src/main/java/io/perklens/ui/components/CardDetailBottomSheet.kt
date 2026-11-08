package io.perklens.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import io.perklens.core.ai.CardAiVerdictEngine
import io.perklens.data.model.Card
import io.perklens.data.repository.CategoryOffersRepository
import io.perklens.data.repository.UserWalletRepository
import io.perklens.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailBottomSheet(
    card: Card,
    userCards: List<Card>,
    aiSettings: AiSettingsRepository,
    categoryOffersRepo: CategoryOffersRepository? = null,
    walletRepo: UserWalletRepository? = null,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val verdict = remember(card) { CardAiVerdictEngine.evaluateOffline(card) }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("AI Verdict", "Perks & Availed", "Billing & Grace", "Multipliers")

    var isLlmLoading by remember { mutableStateOf(false) }
    var llmAnalysisText by remember { mutableStateOf<String?>(null) }

    // Usage data & Billing cycle
    val usageData = walletRepo?.getUsage(card.id)
    var billingDay by remember(usageData) { mutableStateOf(usageData?.billingCycleDay ?: 15) }
    val graceDays = remember(billingDay, walletRepo) {
        walletRepo?.calculateGracePeriodDays(billingDay) ?: 45
    }

    val cardOffers = remember(card) {
        categoryOffersRepo?.getOffersForCard(card.id) ?: emptyList()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        dragHandle = { BottomSheetDefaults.DragHandle(color = DarkBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 36.dp)
        ) {
            // Visual Card
            CreditCardVisual(card = card)

            Spacer(modifier = Modifier.height(14.dp))

            // Navigation Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = DarkSurfaceVariant,
                contentColor = TextPrimary,
                edgePadding = 0.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, DarkBorder, RoundedCornerShape(10.dp))
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selectedContentColor = EmeraldGreen,
                        unselectedContentColor = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            when (selectedTab) {
                0 -> {
                    // TAB 0: AI Verdict
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurfaceVariant)
                            .border(1.5.dp, EmeraldGreen.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(EmeraldGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "AI VERDICT",
                                        color = EmeraldGreen,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = verdict.verdictTag,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(EmeraldGreen)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${verdict.score} / 10",
                                    color = DarkBackground,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = verdict.oneLiner,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(text = "Key Advantages:", color = EmeraldGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        verdict.pros.forEach { pro ->
                            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                Text(text = "✓ ", color = EmeraldGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(text = pro, color = TextPrimary, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (verdict.cons.isNotEmpty()) {
                            Text(text = "Hidden Clauses & Exclusions:", color = AmberGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            verdict.cons.forEach { con ->
                                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                    Text(text = "⚠ ", color = AmberGold, fontSize = 12.sp)
                                    Text(text = con, color = TextSecondary, fontSize = 12.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "🎯 Ideal User: ${verdict.whoShouldApply}",
                            color = ElectricCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (llmAnalysisText == null) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    isLlmLoading = true
                                    llmAnalysisText = CardAiVerdictEngine.fetchLlmVerdict(card, userCards, aiSettings)
                                    isLlmLoading = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkBorder,
                                contentColor = ElectricCyan
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (isLlmLoading) {
                                CircularProgressIndicator(color = ElectricCyan, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Querying AI Model...", fontSize = 13.sp)
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Deep AI Analysis (${aiSettings.selectedProvider.value.displayName})", fontSize = 13.sp)
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(DarkSurfaceVariant)
                                .border(1.dp, ElectricCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Deep AI Breakdown", color = ElectricCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = llmAnalysisText!!,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                1 -> {
                    // TAB 1: Perks & Monthly Availed Tracker
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Monthly Quota & Availed Tracker",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // 1. Movie Tickets Availed
                        val moviesUsed = usageData?.availedMovieTicketsMonth ?: 0
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkSurfaceVariant)
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "🎬 Movie Tickets Availed", color = AmberGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "$moviesUsed free tickets claimed this month", color = TextSecondary, fontSize = 11.sp)
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    OutlinedButton(
                                        onClick = { walletRepo?.recordAvailedMovieTicket(card.id, -1) },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
                                    ) {
                                        Text("-", fontSize = 14.sp)
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Button(
                                        onClick = { walletRepo?.recordAvailedMovieTicket(card.id, 1) },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = AmberGold, contentColor = DarkBackground)
                                    ) {
                                        Text("+ Claim", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // 2. Airport Lounge Visits Availed
                        val loungeUsed = usageData?.availedLoungeVisitsQuarter ?: 0
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkSurfaceVariant)
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "✈️ Airport Lounge Visits", color = ElectricCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "$loungeUsed visits used this calendar quarter", color = TextSecondary, fontSize = 11.sp)
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    OutlinedButton(
                                        onClick = { walletRepo?.recordAvailedLoungeVisit(card.id, -1) },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
                                    ) {
                                        Text("-", fontSize = 14.sp)
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Button(
                                        onClick = { walletRepo?.recordAvailedLoungeVisit(card.id, 1) },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = DarkBackground)
                                    ) {
                                        Text("+ Visit", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // 3. Monthly Cashback Cap Availed
                        val cashbackUsed = usageData?.availedCashbackMonthInr ?: 0.0
                        val capRule = card.merchantRules.find { it.monthlyCapInr != null && it.monthlyCapInr > 0 }
                        val monthlyCap = capRule?.monthlyCapInr ?: 5000.0

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkSurfaceVariant)
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "💰 Monthly Cashback Pool", color = EmeraldGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "₹${cashbackUsed.toInt()} / ₹${monthlyCap.toInt()} Cap Availed", color = TextSecondary, fontSize = 11.sp)
                                }

                                Button(
                                    onClick = { walletRepo?.recordAvailedCashback(card.id, 500.0) },
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen, contentColor = DarkBackground)
                                ) {
                                    Text("+₹500", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            LinearProgressIndicator(
                                progress = (cashbackUsed / monthlyCap).coerceIn(0.0, 1.0).toFloat(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = EmeraldGreen,
                                trackColor = DarkSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Platform Specific Privileges",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        cardOffers.forEach { offer ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DarkSurfaceVariant)
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = offer.platform, color = AmberGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = offer.category.uppercase(), color = ElectricCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = offer.offerTitle, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(text = offer.discountValue, color = TextSecondary, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Terms: ${offer.quotaTerms}", color = TextTertiary, fontSize = 11.sp)
                            }
                        }
                    }
                }

                2 -> {
                    // TAB 2: Billing Cycle & Interest-Free Grace Runway
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurfaceVariant)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Billing Cycle & Grace Runway",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Set your statement date to track repayment runways and cap resets",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Current Grace Days Hero Banner
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(EmeraldGreen.copy(alpha = 0.15f))
                                .border(1.dp, EmeraldGreen.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Swiping Today Gives You:",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "$graceDays Days Interest-Free!",
                                    color = EmeraldGreen,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Statement Generation Day: Day $billingDay of each month",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Slider(
                            value = billingDay.toFloat(),
                            onValueChange = {
                                billingDay = it.toInt()
                                walletRepo?.setBillingCycleDay(card.id, billingDay)
                            },
                            valueRange = 1f..31f,
                            steps = 30,
                            colors = SliderDefaults.colors(
                                thumbColor = EmeraldGreen,
                                activeTrackColor = EmeraldGreen,
                                inactiveTrackColor = DarkBorder
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "• Statement generates on the ${billingDay}th of every month\n• Monthly caps & cinema quotas auto-reset on this day\n• Payment due date is approximately ~20 days later",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                3 -> {
                    // TAB 3: Multipliers & Cap Rules
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Reward Multiplier Structure",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        card.merchantRules.forEach { rule ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DarkSurfaceVariant)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = rule.category.replace("_", " ").capitalize(), color = TextPrimary, fontSize = 13.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${rule.cashbackPercent}%",
                                        color = if (rule.cashbackPercent > 0) EmeraldGreen else AmberGold,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (rule.monthlyCapInr != null && rule.monthlyCapInr > 0) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "(Cap ₹${rule.monthlyCapInr.toInt()}/mo)", color = TextSecondary, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
