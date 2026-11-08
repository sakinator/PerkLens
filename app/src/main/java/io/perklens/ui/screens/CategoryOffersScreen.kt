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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.perklens.data.model.Card
import io.perklens.data.model.CategoryPlatformOffer
import io.perklens.data.repository.CardRepository
import io.perklens.data.repository.CategoryOffersRepository
import io.perklens.data.repository.UserWalletRepository
import io.perklens.ui.theme.*

data class OfferCategoryTab(val key: String, val title: String, val icon: String)

@Composable
fun CategoryOffersScreen(
    categoryOffersRepo: CategoryOffersRepository,
    walletRepo: UserWalletRepository,
    cardRepo: CardRepository
) {
    val savedCardIds by walletRepo.savedCardIds.collectAsState()

    val categories = listOf(
        OfferCategoryTab("movies", "Movies & Cinema", "🎬"),
        OfferCategoryTab("dining", "Dining & Food", "🍽️"),
        OfferCategoryTab("shopping", "Online Shopping", "🛍️"),
        OfferCategoryTab("grocery", "Quick Groceries", "⚡"),
        OfferCategoryTab("utility", "Bills & Recharge", "💡"),
        OfferCategoryTab("travel", "Travel & Flights", "✈️"),
        OfferCategoryTab("fuel", "Fuel Surcharge", "⛽")
    )

    var selectedCategoryKey by remember { mutableStateOf("movies") }
    var filterUserCardsOnly by remember { mutableStateOf(true) }

    val currentOffers = remember(selectedCategoryKey, filterUserCardsOnly, savedCardIds) {
        if (filterUserCardsOnly) {
            categoryOffersRepo.getOffersForCategory(selectedCategoryKey, savedCardIds)
        } else {
            categoryOffersRepo.getAllOffersForCategory(selectedCategoryKey)
        }
    }

    val selectedCategoryObj = remember(selectedCategoryKey) {
        categories.find { it.key == selectedCategoryKey } ?: categories.first()
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
                    text = "Card Privilege Offers",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Select a category to see what offers your cards provide",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(AmberGold.copy(alpha = 0.15f))
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = null,
                    tint = AmberGold,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Category Horizontal Selector Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { tab ->
                val isSelected = selectedCategoryKey == tab.key
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategoryKey = tab.key },
                    label = { Text("${tab.icon} ${tab.title}", fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AmberGold.copy(alpha = 0.2f),
                        selectedLabelColor = AmberGold,
                        containerColor = DarkSurfaceVariant,
                        labelColor = TextSecondary
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) AmberGold else DarkBorder
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Toggle: "My Cards Only" vs "All Market Cards"
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
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (filterUserCardsOnly) "Showing Offers for My Saved Cards" else "Showing All Market Card Offers",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Switch(
                checked = filterUserCardsOnly,
                onCheckedChange = { filterUserCardsOnly = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = DarkBackground,
                    checkedTrackColor = EmeraldGreen,
                    uncheckedTrackColor = DarkBorder
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Offers List
        if (currentOffers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "No ${selectedCategoryObj.title} offers in your current wallet",
                        color = TextSecondary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Toggle off 'My Saved Cards' above to explore top market cards offering ${selectedCategoryObj.title} privileges.",
                        color = TextTertiary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(currentOffers) { (card, offer) ->
                    CardCategoryOfferItem(card = card, offer = offer)
                }
            }
        }
    }
}

@Composable
fun CardCategoryOfferItem(
    card: Card,
    offer: CategoryPlatformOffer
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Platform Badge & Card Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                // Platform Logo or Pill
                if (!offer.platformLogoUrl.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .height(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White)
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = offer.platformLogoUrl,
                            contentDescription = offer.platform,
                            modifier = Modifier.fillMaxHeight(),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }

                Column {
                    Text(
                        text = offer.platform,
                        color = AmberGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = card.name,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(EmeraldGreen.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = card.issuer.uppercase(),
                    color = EmeraldGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Offer Title & Value
        Text(
            text = offer.offerTitle,
            color = ElectricCyan,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = offer.discountValue,
            color = TextPrimary,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Terms & Redemption Box
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(DarkSurfaceVariant)
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Quota: ${offer.quotaTerms}",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "How to Apply: ${offer.redemptionMethod}",
                    color = TextPrimary,
                    fontSize = 11.sp
                )
            }
        }
    }
}
