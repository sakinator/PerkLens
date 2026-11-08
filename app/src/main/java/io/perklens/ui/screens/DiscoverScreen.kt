package io.perklens.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.*
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
import io.perklens.core.ai.AiSettingsRepository
import io.perklens.core.ai.LiveWebCardDiscoveryManager
import io.perklens.data.model.Card
import io.perklens.data.repository.CardRepository
import io.perklens.data.repository.UserWalletRepository
import io.perklens.ui.components.CardDetailBottomSheet
import io.perklens.ui.components.CreditCardVisual
import io.perklens.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun DiscoverScreen(
    cardRepo: CardRepository,
    walletRepo: UserWalletRepository,
    aiSettings: AiSettingsRepository,
    liveWebDiscoveryManager: LiveWebCardDiscoveryManager? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val savedCardIds by walletRepo.savedCardIds.collectAsState()

    var catalogVersion by remember { mutableStateOf(0) }
    val allCards = remember(catalogVersion) { cardRepo.getAllCards() }
    val userCards = remember(savedCardIds, catalogVersion) {
        allCards.filter { savedCardIds.contains(it.id) }
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedCardForDetail by remember { mutableStateOf<Card?>(null) }

    // Live Web & Network Hub Importer Dialog State
    var showImportDialog by remember { mutableStateOf(false) }
    var importTab by remember { mutableStateOf(0) } // 0: Network Hub Search, 1: Bank URL
    var inputQuery by remember { mutableStateOf("") }
    var isImporting by remember { mutableStateOf(false) }
    var importStatusMessage by remember { mutableStateOf<String?>(null) }
    var importError by remember { mutableStateOf<String?>(null) }
    var discoveredCardPreview by remember { mutableStateOf<Card?>(null) }
    var alsoAddToVault by remember { mutableStateOf(true) }

    val filterOptions = listOf(
        "All" to "All ${allCards.size}+ Cards",
        "BoB" to "Bank of Baroda (Eterna)",
        "HDFC" to "HDFC (Infinia/Swiggy)",
        "SBI" to "SBI (Cashback/Aurum)",
        "ICICI" to "ICICI (Sapphiro/Amazon)",
        "Axis" to "Axis (Magnus/Atlas)",
        "IDFC" to "IDFC FIRST (Wealth/WOW)",
        "Amex" to "American Express",
        "IndusInd" to "IndusInd (Legend/Tiger)",
        "Kotak" to "Kotak (White/Zen)",
        "AU" to "AU Small Finance (Zenith+)",
        "RBL" to "RBL (World Safari)",
        "Federal" to "Federal & Scapia",
        "YesBank" to "Yes Bank & Kiwi",
        "PNB" to "PSU (PNB/Canara/BOI)",
        "StanC" to "Standard Chartered",
        "HSBC" to "HSBC (Live+)",
        "Fintech" to "Fintech (OneCard/Jupiter/Fi)",
        "Debit" to "Debit Cards",
        "LTF" to "Lifetime Free (LTF)",
        "RuPay" to "RuPay UPI",
        "Cashback" to "Cashback Champions",
        "Travel" to "Travel & Forex 0%",
        "Fuel" to "Fuel (BPCL/HPCL/IOCL)",
        "LowFee" to "Low Fee (<₹1000)"
    )

    val displayedCards = remember(selectedFilter, searchQuery, allCards) {
        val filtered = cardRepo.getCardsByFilter(selectedFilter)
        if (searchQuery.isBlank()) {
            filtered
        } else {
            val q = searchQuery.trim().lowercase()
            filtered.filter { card ->
                card.name.lowercase().contains(q) ||
                card.issuer.lowercase().contains(q) ||
                card.bestForCategory.lowercase().contains(q) ||
                card.network.lowercase().contains(q)
            }
        }
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

        Spacer(modifier = Modifier.height(14.dp))

        // Live Network Hub & Web Importer Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    inputQuery = ""
                    importError = null
                    discoveredCardPreview = null
                    showImportDialog = true
                },
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.CloudDownload,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Live Network Hub & Web Importer",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = EmeraldGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "RuPay • Visa • MC",
                                    color = EmeraldGreen,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "Zero missing cards: resolves via Network Hubs & Gemini",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = ElectricCyan,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search ${allCards.size}+ cards (e.g. BoB Eterna, Infinia, Swiggy, LTF)...", color = TextTertiary, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ElectricCyan) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = ElectricCyan,
                unfocusedBorderColor = DarkBorder,
                focusedContainerColor = DarkSurfaceVariant,
                unfocusedContainerColor = DarkSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

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
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) ElectricCyan else DarkBorder
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

    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isImporting) showImportDialog = false
            },
            containerColor = DarkSurface,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Live Network Hub & Web Importer",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Two-Tier Network-to-Bank Resolution Pipeline",
                            color = ElectricCyan,
                            fontSize = 11.sp
                        )
                    }
                    IconButton(
                        onClick = { showImportDialog = false },
                        enabled = !isImporting
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextTertiary)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    // Two Tabs: 0 = Network Hub Search, 1 = Bank URL
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceVariant)
                            .padding(3.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    importTab = 0
                                    importError = null
                                    discoveredCardPreview = null
                                },
                            shape = RoundedCornerShape(6.dp),
                            color = if (importTab == 0) ElectricCyan.copy(alpha = 0.25f) else Color.Transparent
                        ) {
                            Text(
                                text = "1. Network Hub (RuPay/Visa/MC)",
                                color = if (importTab == 0) ElectricCyan else TextTertiary,
                                fontSize = 11.sp,
                                fontWeight = if (importTab == 0) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    importTab = 1
                                    importError = null
                                    discoveredCardPreview = null
                                },
                            shape = RoundedCornerShape(6.dp),
                            color = if (importTab == 1) ElectricCyan.copy(alpha = 0.25f) else Color.Transparent
                        ) {
                            Text(
                                text = "2. Bank Webpage URL",
                                color = if (importTab == 1) ElectricCyan else TextTertiary,
                                fontSize = 11.sp,
                                fontWeight = if (importTab == 1) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (importTab == 0) {
                        Text(
                            text = "Step 1: Identifies card registry on RuPay NPCI, Visa India, Mastercard, or Amex.\nStep 2: Enriches exact reward accelerators, fees & lounge rules via Gemini Grounding.",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = inputQuery,
                            onValueChange = { inputQuery = it },
                            placeholder = { Text("e.g. BoB Eterna, Scapia, Air India SBI, RuPay Select...", color = TextTertiary, fontSize = 12.sp) },
                            singleLine = true,
                            enabled = !isImporting,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = ElectricCyan,
                                unfocusedBorderColor = DarkBorder,
                                focusedContainerColor = DarkSurfaceVariant,
                                unfocusedContainerColor = DarkSurfaceVariant
                            )
                        )
                    } else {
                        Text(
                            text = "Paste any official bank landing page URL (HDFC, SBI, BoB, ICICI, Axis, PNB, etc.). PerkLens fetches HTML directly and extracts card privileges.",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = inputQuery,
                            onValueChange = { inputQuery = it },
                            placeholder = { Text("https://www.bankofbaroda.in/.../eterna", color = TextTertiary, fontSize = 12.sp) },
                            singleLine = true,
                            enabled = !isImporting,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = ElectricCyan,
                                unfocusedBorderColor = DarkBorder,
                                focusedContainerColor = DarkSurfaceVariant,
                                unfocusedContainerColor = DarkSurfaceVariant
                            )
                        )
                    }

                    if (isImporting) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                color = ElectricCyan,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (importTab == 0) "Resolving Network Hub & Enriching via Gemini..." else "Fetching bank HTML & parsing card...",
                                color = ElectricCyan,
                                fontSize = 12.sp
                            )
                        }
                    }

                    if (importError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "⚠️ $importError",
                            color = CoralRed,
                            fontSize = 12.sp
                        )
                    }

                    // Card Preview if resolved
                    if (discoveredCardPreview != null) {
                        val preview = discoveredCardPreview!!
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = DarkSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(preview.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("${preview.issuer} • ${preview.network}", color = EmeraldGreen, fontSize = 12.sp)
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (preview.isLifetimeFree) EmeraldGreen.copy(alpha = 0.2f) else DarkBorder
                                    ) {
                                        Text(
                                            text = if (preview.isLifetimeFree) "LTF" else "₹${preview.annualFee}/yr",
                                            color = if (preview.isLifetimeFree) EmeraldGreen else TextPrimary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("✈️ ${preview.loungeAccess}", color = TextSecondary, fontSize = 11.sp)
                                Text("🎯 Best For: ${preview.bestForCategory} (${preview.rewardType})", color = ElectricCyan, fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { alsoAddToVault = !alsoAddToVault }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = alsoAddToVault,
                                onCheckedChange = { alsoAddToVault = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = EmeraldGreen,
                                    checkmarkColor = DarkBackground
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Also add this card to My Vault", color = TextPrimary, fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                if (discoveredCardPreview == null) {
                    Button(
                        onClick = {
                            if (inputQuery.isBlank()) {
                                importError = "Please enter a card name or URL."
                                return@Button
                            }
                            isImporting = true
                            importError = null
                            coroutineScope.launch {
                                try {
                                    val result = if (importTab == 0) {
                                        liveWebDiscoveryManager?.discoverAndEnrichCard(inputQuery.trim(), aiSettings)
                                    } else {
                                        liveWebDiscoveryManager?.importCardFromBankUrl(inputQuery.trim(), aiSettings)
                                    }
                                    if (result != null && result.isSuccess) {
                                        discoveredCardPreview = result.getOrNull()
                                    } else {
                                        importError = result?.exceptionOrNull()?.localizedMessage ?: "Could not resolve card."
                                    }
                                } catch (e: Exception) {
                                    importError = e.localizedMessage ?: "Failed to resolve card"
                                } finally {
                                    isImporting = false
                                }
                            }
                        },
                        enabled = !isImporting && inputQuery.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan)
                    ) {
                        Text(
                            text = if (importTab == 0) "Resolve & Enrich" else "Fetch & Extract",
                            color = DarkBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            discoveredCardPreview?.let { card ->
                                cardRepo.addOrUpdateCard(card)
                                if (alsoAddToVault) {
                                    walletRepo.addCard(card.id)
                                }
                                catalogVersion++
                                Toast.makeText(context, "Added '${card.name}' to PerkLens!", Toast.LENGTH_SHORT).show()
                                showImportDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                    ) {
                        Text("✓ Save to Database", color = DarkBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showImportDialog = false },
                    enabled = !isImporting
                ) {
                    Text("Cancel", color = TextTertiary, fontSize = 12.sp)
                }
            }
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
