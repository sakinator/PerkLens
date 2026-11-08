package io.perklens.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.perklens.data.model.LiveOffer
import io.perklens.data.repository.CardRepository
import io.perklens.ui.theme.*

@Composable
fun OffersScreen(
    cardRepo: CardRepository
) {
    val allOffers = remember { cardRepo.getLiveOffers() }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Dining & Food Delivery", "Electronics & Shopping", "Movies & Entertainment", "Flights & Travel")

    val filteredOffers = remember(selectedCategory) {
        if (selectedCategory == "All") allOffers
        else allOffers.filter { it.category.contains(selectedCategory, ignoreCase = true) }
    }

    val context = LocalContext.current

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
                    text = "Live Card Offers",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "SaveSage-style merchant discounts & coupon deals",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = null,
                tint = AmberGold,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = selectedCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat, fontSize = 12.sp) },
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

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(filteredOffers) { offer ->
                OfferCardItem(offer = offer, onCopy = { code ->
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Coupon Code", code))
                    Toast.makeText(context, "Copied code: $code", Toast.LENGTH_SHORT).show()
                })
            }
        }
    }
}

@Composable
fun OfferCardItem(
    offer: LiveOffer,
    onCopy: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Brand & Bank Header with Authentic Logos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Proper Brand Logo Badge
                if (!offer.brandLogoUrl.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .border(1.dp, DarkBorder, RoundedCornerShape(10.dp))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = offer.brandLogoUrl,
                            contentDescription = offer.brand,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }

                Column {
                    Text(
                        text = offer.brand,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Bank Logo / Issuer
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!offer.bankLogoUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = offer.bankLogoUrl,
                                contentDescription = offer.bank,
                                modifier = Modifier
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = offer.bank,
                            color = ElectricCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Text(
                text = "Valid: ${offer.validTill}",
                color = TextTertiary,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = offer.title,
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = offer.discountSummary,
            color = TextSecondary,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Coupon Code Bar & Copy
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(DarkSurfaceVariant)
                .border(1.dp, DarkBorder, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CODE: ${offer.couponCode}",
                color = AmberGold,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = { onCopy(offer.couponCode) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkBorder,
                    contentColor = TextPrimary
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                shape = RoundedCornerShape(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy Code",
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Copy", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Terms: ${offer.terms}",
            color = TextTertiary,
            fontSize = 11.sp,
            lineHeight = 15.sp
        )
    }
}
