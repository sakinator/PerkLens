package io.perklens.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import io.perklens.data.model.Card
import io.perklens.ui.theme.*

@Composable
fun CreditCardVisual(
    card: Card,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Neon Green Gradient as default theme
    val startColor = try {
        Color(android.graphics.Color.parseColor(card.cardGradientStart))
    } catch (e: Exception) {
        CardNeonStart
    }

    val endColor = try {
        Color(android.graphics.Color.parseColor(card.cardGradientEnd))
    } catch (e: Exception) {
        CardNeonEnd
    }

    // Standard credit card CR80 ratio: 1.586
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.586f)
            .shadow(20.dp, RoundedCornerShape(16.dp), spotColor = NeonGreen.copy(alpha = 0.4f))
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(startColor, endColor)
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonGreenBright.copy(alpha = 0.8f),
                        NeonGreen.copy(alpha = 0.4f),
                        Color.White.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        // If real card image URL is available, attempt to render it with SubcomposeAsyncImage
        if (!card.imageUrl.isNullOrBlank()) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(card.imageUrl)
                    .setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
                    .crossfade(true)
                    .build(),
                contentDescription = card.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = NeonGreen,
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 2.dp
                        )
                    }
                },
                error = {
                    // Fallback to high-fidelity neon synthetic card if image fetch fails
                    FallbackCardDesign(card = card, startColor = startColor, endColor = endColor)
                }
            )
        } else {
            FallbackCardDesign(card = card, startColor = startColor, endColor = endColor)
        }

        // Realistic Plastic Lamination Specular Sheen (Subtle neon light reflection across diagonal)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            NeonGreenBright.copy(alpha = 0.15f),
                            Color.Transparent,
                            Color.White.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Overlay bank badge & network emblem on top for crisp typography
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Bank Logo or Text
                if (!card.bankLogoUrl.isNullOrBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .height(26.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkBackground.copy(alpha = 0.85f))
                                .border(0.5.dp, DarkBorderNeon, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            AsyncImage(
                                model = card.bankLogoUrl,
                                contentDescription = card.issuer,
                                modifier = Modifier.height(20.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                } else {
                    Text(
                        text = card.issuer.uppercase(),
                        color = Color.White.copy(alpha = 0.95f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Card Type Pill (CREDIT / DEBIT)
                    val isDebit = card.cardType.equals("DEBIT", ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDebit) ElectricCyan.copy(alpha = 0.25f) else NeonGreen.copy(alpha = 0.2f))
                            .border(0.5.dp, if (isDebit) ElectricCyan else NeonGreen, RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (isDebit) "DEBIT" else "CREDIT",
                            color = if (isDebit) ElectricCyan else NeonGreenBright,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkBackground.copy(alpha = 0.6f))
                            .border(0.5.dp, DarkBorderNeon, RoundedCornerShape(12.dp))
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = OfferBadgeGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${card.rating}",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Bottom Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f, fill = false)) {
                    Text(
                        text = card.name,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color = Color.Black.copy(alpha = 0.8f),
                                blurRadius = 8f
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = if (card.isLifetimeFree) "LIFETIME FREE" else "ANNUAL: ₹${card.annualFee}",
                        color = if (card.isLifetimeFree) NeonGreenBright else RadiantGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Network Emblem
                CardNetworkBadge(network = card.network)
            }
        }
    }
}

@Composable
fun CardNetworkBadge(network: String, modifier: Modifier = Modifier) {
    val cleanNet = network.trim().uppercase()
    when {
        cleanNet.contains("RUPAY") -> {
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(DarkBackground.copy(alpha = 0.85f))
                    .border(0.8.dp, DarkBorderNeon, RoundedCornerShape(6.dp))
                    .padding(horizontal = 7.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RuPay",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.width(3.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFFF5F00))
                )
                Spacer(modifier = Modifier.width(2.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFF00A651))
                )
            }
        }
        cleanNet.contains("MASTER") -> {
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(DarkBackground.copy(alpha = 0.85f))
                    .border(0.8.dp, DarkBorderNeon, RoundedCornerShape(6.dp))
                    .padding(horizontal = 7.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(11.dp).clip(CircleShape).background(Color(0xFFEB001B)))
                Box(modifier = Modifier.offset(x = (-4).dp).size(11.dp).clip(CircleShape).background(Color(0xFFF79E1B).copy(alpha = 0.9f)))
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "mastercard",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        cleanNet.contains("AMEX") || cleanNet.contains("AMERICAN") -> {
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF006FCF))
                    .border(0.8.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "AMEX",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }
        cleanNet.contains("DINERS") -> {
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF004A80))
                    .border(0.8.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DINERS CLUB",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        else -> {
            // Default to Visa
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(DarkBackground.copy(alpha = 0.85f))
                    .border(0.8.dp, DarkBorderNeon, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "VISA",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

@Composable
private fun FallbackCardDesign(
    card: Card,
    startColor: Color,
    endColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(startColor, endColor)
                )
            )
            .padding(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Middle section: EMV Gold Chip & Contactless Wave
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Photorealistic EMV Gold Chip
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            brush = Brush.linearGradient(
                                listOf(Color(0xFFFFE082), Color(0xFFFFB300), Color(0xFFB8860B))
                            )
                        )
                        .border(1.dp, Color(0xFF8B6508), RoundedCornerShape(6.dp))
                        .padding(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(0.5.dp, Color(0xFF6D4C41).copy(alpha = 0.7f), RoundedCornerShape(3.dp))
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "))))",
                    color = NeonGreenBright.copy(alpha = 0.85f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Bottom section: Masked Number & Cardholder Info
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "••••  ••••  ••••  8824",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp,
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = androidx.compose.ui.graphics.Shadow(
                            color = Color.Black.copy(alpha = 0.9f),
                            blurRadius = 6f
                        )
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PERKLENS VIP",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "VALID 12/30",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
