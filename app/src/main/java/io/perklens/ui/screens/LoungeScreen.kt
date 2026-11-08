package io.perklens.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import io.perklens.data.model.Lounge
import io.perklens.data.model.LoungeAccessCriteria
import io.perklens.data.model.LoungeType
import io.perklens.data.repository.CardRepository
import io.perklens.data.repository.LoungeRepository
import io.perklens.data.repository.UserWalletRepository
import io.perklens.ui.theme.*

@Composable
fun LoungeScreen(
    loungeRepo: LoungeRepository,
    cardRepo: CardRepository,
    walletRepo: UserWalletRepository
) {
    val savedCardIds by walletRepo.savedCardIds.collectAsState()
    val allLounges = remember { loungeRepo.getAllLounges() }

    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<LoungeType?>(null) }
    var showMyEligibleOnly by remember { mutableStateOf(false) }

    val filteredLounges = remember(searchQuery, selectedType, showMyEligibleOnly, savedCardIds) {
        allLounges.filter { lounge ->
            val matchesSearch = searchQuery.isBlank() ||
                lounge.city.contains(searchQuery, ignoreCase = true) ||
                lounge.airportOrStation.contains(searchQuery, ignoreCase = true) ||
                lounge.name.contains(searchQuery, ignoreCase = true) ||
                lounge.terminal.contains(searchQuery, ignoreCase = true)

            val matchesType = selectedType == null || lounge.type == selectedType

            val matchesEligible = !showMyEligibleOnly ||
                lounge.eligibleCardIds.any { savedCardIds.contains(it) }

            matchesSearch && matchesType && matchesEligible
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Lounge Pass Hub",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Airport & railway lounges with live spend-criteria tracking",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(ElectricCyan.copy(alpha = 0.15f))
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FlightTakeoff,
                    contentDescription = null,
                    tint = ElectricCyan,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search city, airport or terminal (DEL, BOM, BLR...)") },
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
                focusedBorderColor = ElectricCyan,
                unfocusedBorderColor = DarkBorder,
                focusedContainerColor = DarkSurfaceVariant,
                unfocusedContainerColor = DarkSurfaceVariant
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Type Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedType == null,
                onClick = { selectedType = null },
                label = { Text("All (${allLounges.size})", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ElectricCyan.copy(alpha = 0.2f),
                    selectedLabelColor = ElectricCyan,
                    containerColor = DarkSurfaceVariant,
                    labelColor = TextSecondary
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedType == null) ElectricCyan else DarkBorder)
            )

            FilterChip(
                selected = selectedType == LoungeType.AIRPORT_DOMESTIC,
                onClick = { selectedType = LoungeType.AIRPORT_DOMESTIC },
                label = { Text("🛫 Domestic", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ElectricCyan.copy(alpha = 0.2f),
                    selectedLabelColor = ElectricCyan,
                    containerColor = DarkSurfaceVariant,
                    labelColor = TextSecondary
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedType == LoungeType.AIRPORT_DOMESTIC) ElectricCyan else DarkBorder)
            )

            FilterChip(
                selected = selectedType == LoungeType.AIRPORT_INTERNATIONAL,
                onClick = { selectedType = LoungeType.AIRPORT_INTERNATIONAL },
                label = { Text("🌍 International", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ElectricCyan.copy(alpha = 0.2f),
                    selectedLabelColor = ElectricCyan,
                    containerColor = DarkSurfaceVariant,
                    labelColor = TextSecondary
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedType == LoungeType.AIRPORT_INTERNATIONAL) ElectricCyan else DarkBorder)
            )

            FilterChip(
                selected = selectedType == LoungeType.RAILWAY,
                onClick = { selectedType = LoungeType.RAILWAY },
                label = { Text("🚆 Railway", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AmberGold.copy(alpha = 0.2f),
                    selectedLabelColor = AmberGold,
                    containerColor = DarkSurfaceVariant,
                    labelColor = TextSecondary
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedType == LoungeType.RAILWAY) AmberGold else DarkBorder)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // "My Eligible Lounges Only" Toggle Switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(DarkSurfaceVariant)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CreditCard, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "My Cards Eligible Only",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Switch(
                checked = showMyEligibleOnly,
                onCheckedChange = { showMyEligibleOnly = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = DarkBackground,
                    checkedTrackColor = EmeraldGreen,
                    uncheckedTrackColor = DarkBorder
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lounge Items List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(filteredLounges) { lounge ->
                val matchingUserCards = lounge.eligibleCardIds
                    .filter { savedCardIds.contains(it) }
                    .mapNotNull { cardRepo.getCardById(it) }

                LoungeItemCard(
                    lounge = lounge,
                    matchingUserCards = matchingUserCards
                )
            }
        }
    }
}

@Composable
fun LoungeItemCard(
    lounge: Lounge,
    matchingUserCards: List<io.perklens.data.model.Card>
) {
    val isEligible = matchingUserCards.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(
                1.dp,
                if (isEligible) EmeraldGreen.copy(alpha = 0.6f) else DarkBorder,
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        // City & Status Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DarkSurfaceVariant)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = lounge.city.uppercase(),
                        color = ElectricCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = lounge.terminal,
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }

            // Eligibility Pill
            if (isEligible) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(EmeraldGreen.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "YOU CAN ENTER ✓",
                        color = EmeraldGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DarkBorder)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Card Needed",
                        color = TextTertiary,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lounge Name & Airport
        Text(
            text = lounge.name,
            color = TextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = lounge.airportOrStation,
            color = TextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Access Criteria Badge & Spend Requirement Note
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(DarkSurfaceVariant)
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val (criteriaText, criteriaColor) = when (lounge.criteria) {
                    LoungeAccessCriteria.UNCONDITIONAL -> Pair("UNCONDITIONAL (₹2 SWIPE)", EmeraldGreen)
                    LoungeAccessCriteria.SPEND_LINKED -> Pair("SPEND-LINKED REQUIREMENT", AmberGold)
                    LoungeAccessCriteria.PRIORITY_PASS -> Pair("PRIORITY PASS / DREAMFOLKS", ElectricCyan)
                    LoungeAccessCriteria.PAID_ONLY -> Pair("PAID ADMISSION ONLY", TextTertiary)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(criteriaColor.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = criteriaText,
                        color = criteriaColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = lounge.spendRequirementNote,
                color = TextPrimary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }

        // Matching User Cards
        if (isEligible) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Your Access Card: ",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Text(
                    text = matchingUserCards.joinToString(", ") { it.name },
                    color = EmeraldGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Location & Timings
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "📍 ${lounge.locationDetail}",
                color = TextTertiary,
                fontSize = 10.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "⏰ ${lounge.timings}",
                color = TextTertiary,
                fontSize = 10.sp
            )
        }
    }
}
