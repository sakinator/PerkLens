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

            // Bottom Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = card.name,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
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

                // Network Logo
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DarkBackground.copy(alpha = 0.75f))
                        .border(0.5.dp, DarkBorderNeon, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = card.network.uppercase(),
                        color = NeonGreenBright,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
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
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Simulated chip & contactless in metallic gold & neon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(42.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            brush = Brush.linearGradient(
                                listOf(Color(0xFFFFDF00), Color(0xFFB8860B))
                            )
                        )
                        .border(1.dp, Color(0xFF8B6508), RoundedCornerShape(6.dp))
                )

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = "))))",
                    color = NeonGreenBright.copy(alpha = 0.85f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Card Number
            Text(
                text = "••••  ••••  ••••  8824",
                color = TextPrimary.copy(alpha = 0.85f),
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp
            )
        }
    }
}
