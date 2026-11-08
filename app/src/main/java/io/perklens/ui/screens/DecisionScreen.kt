package io.perklens.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.perklens.data.model.DecisionResult
import io.perklens.data.repository.CardRepository
import io.perklens.data.repository.UserWalletRepository
import io.perklens.engine.RewardDecisionEngine
import io.perklens.ui.theme.*

data class SpendMerchant(val key: String, val label: String, val icon: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecisionScreen(
    cardRepo: CardRepository,
    walletRepo: UserWalletRepository
) {
    val savedCardIds by walletRepo.savedCardIds.collectAsState()
    val allCards = remember { cardRepo.getAllCards() }
    val userCards = remember(savedCardIds) {
        allCards.filter { savedCardIds.contains(it.id) }
    }

    var amountText by remember { mutableStateOf("2000") }
    var selectedMerchant by remember { mutableStateOf("swiggy") }

    val merchants = listOf(
        SpendMerchant("swiggy", "Swiggy", "🍔"),
        SpendMerchant("zomato", "Zomato", "🍕"),
        SpendMerchant("amazon", "Amazon", "📦"),
        SpendMerchant("flipkart", "Flipkart", "🛍️"),
        SpendMerchant("blinkit", "Blinkit", "⚡"),
        SpendMerchant("utility", "Utilities / Bill", "💡"),
        SpendMerchant("flight", "Flights / Travel", "✈️"),
        SpendMerchant("offline_general", "Offline Swipe", "🏪")
    )

    val quickAmounts = listOf("500", "1000", "2000", "5000", "10000")

    val amount = amountText.toDoubleOrNull() ?: 0.0

    val decisionResults = remember(userCards, amount, selectedMerchant) {
        RewardDecisionEngine.evaluateBestCard(userCards, amount, selectedMerchant)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "Smart Swipe Optimizer",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Calculate which card earns you the highest cashback & reward points",
            color = TextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Amount Input Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurface)
                .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Transaction Amount (₹)",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = amountText,
                onValueChange = { input ->
                    if (input.all { it.isDigit() || it == '.' }) {
                        amountText = input
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                prefix = { Text("₹ ", color = EmeraldGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
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

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Amount Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quickAmounts.forEach { amt ->
                    FilterChip(
                        selected = amountText == amt,
                        onClick = { amountText = amt },
                        label = { Text("₹$amt", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EmeraldGreen.copy(alpha = 0.2f),
                            selectedLabelColor = EmeraldGreen,
                            containerColor = DarkSurfaceVariant,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (amountText == amt) EmeraldGreen else DarkBorder,
                            selectedBorderColor = EmeraldGreen
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Merchant Selector Chips
        Text(
            text = "Select Merchant / Category",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            merchants.forEach { m ->
                val isSelected = selectedMerchant == m.key
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedMerchant = m.key },
                    label = { Text("${m.icon} ${m.label}", fontSize = 12.sp) },
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

        // Decision Rankings List
        Text(
            text = "Ranked Recommendations",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (userCards.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add cards in the Wallet tab to calculate savings",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(decisionResults) { res ->
                    DecisionResultCard(res = res)
                }
            }
        }
    }
}

@Composable
fun DecisionResultCard(res: DecisionResult) {
    val borderColor = if (res.isBestChoice) EmeraldGreen else DarkBorder
    val bgColor = if (res.isBestChoice) DarkSurfaceVariant else DarkSurface

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(if (res.isBestChoice) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (res.isBestChoice) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(EmeraldGreen)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "BEST 🥇",
                            color = DarkBackground,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Column {
                    Text(
                        text = res.card.name,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = res.card.issuer,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            // Savings Value
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${res.savingsInr.toInt()}",
                    color = if (res.isBestChoice) EmeraldGreen else TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${res.effectivePercent}% back",
                    color = if (res.effectivePercent > 0) ElectricCyan else TextTertiary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (res.capNote != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = AmberGold,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = res.capNote,
                    color = AmberGold,
                    fontSize = 11.sp
                )
            }
        }
    }
}
