package io.perklens.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import io.perklens.core.ai.AiProvider
import io.perklens.core.ai.AiSettingsRepository
import io.perklens.core.parser.IntelligentOfferExhaustionEngine
import io.perklens.core.parser.SmsSyncManager
import io.perklens.core.sync.GmailSyncManager
import io.perklens.data.repository.CardRepository
import io.perklens.data.repository.UserWalletRepository
import io.perklens.data.sync.RemoteCatalogSyncManager
import io.perklens.data.sync.SyncState
import io.perklens.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    aiSettings: AiSettingsRepository,
    syncManager: RemoteCatalogSyncManager,
    cardRepo: CardRepository,
    walletRepo: UserWalletRepository? = null,
    smsSyncManager: SmsSyncManager? = null,
    gmailSyncManager: GmailSyncManager? = null,
    onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val currentProvider by aiSettings.selectedProvider.collectAsState()
    val suggestLtfOnly by aiSettings.suggestLtfOnly.collectAsState()
    val syncState by syncManager.syncState.collectAsState()

    val isSmsSyncEnabled by (smsSyncManager?.isSmsSyncEnabled?.collectAsState() ?: remember { mutableStateOf(false) })
    val isGmailSyncEnabled by (gmailSyncManager?.isConnected?.collectAsState() ?: remember { mutableStateOf(false) })
    val recentTransactions by (smsSyncManager?.recentTransactions?.collectAsState() ?: remember { mutableStateOf(emptyList()) })

    val savedCardIds by (walletRepo?.savedCardIds?.collectAsState() ?: remember { mutableStateOf(emptySet()) })
    val cardUsages by (walletRepo?.cardUsages?.collectAsState() ?: remember { mutableStateOf(emptyMap()) })

    val userCards = remember(savedCardIds) {
        cardRepo.getAllCards().filter { savedCardIds.contains(it.id) }
    }

    val billingDays = remember(cardUsages) {
        cardUsages.mapValues { it.value.billingCycleDay }
    }

    // Intelligent Exhaustion Analysis
    val exhaustionStatuses = remember(recentTransactions, userCards, billingDays) {
        IntelligentOfferExhaustionEngine.analyzeExhaustion(recentTransactions, userCards, billingDays)
    }

    var isScanningSms by remember { mutableStateOf(false) }

    var apiKeyText by remember(currentProvider) {
        mutableStateOf(aiSettings.getApiKey(currentProvider))
    }
    var isKeyVisible by remember { mutableStateOf(false) }
    var syncUrlText by remember { mutableStateOf(syncManager.getSyncUrl()) }

    // SMS Permission Launcher
    val smsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            smsSyncManager?.setSmsSyncEnabled(true)
            Toast.makeText(context, "SMS permission granted! Ready to scan.", Toast.LENGTH_SHORT).show()
        } else {
            smsSyncManager?.setSmsSyncEnabled(false)
            Toast.makeText(context, "SMS permission denied.", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { Text("Settings & Intelligent Sync", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface,
                    titleContentColor = TextPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. Automated Spends & Exhausted Offers Tracking (SMS & Gmail)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MarkEmailRead, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Automated SMS & Gmail Sync",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Intelligently parses bank transactions to track used monthly cinema tickets, quarterly airport lounges, and annual fee waiver milestones.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Privacy Shield Box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(EmeraldGreen.copy(alpha = 0.12f))
                        .border(0.5.dp, EmeraldGreen.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🔒 100% On-Device Processing. No SMS or email text is ever uploaded or leaves your device.",
                        color = EmeraldGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // SMS Sync Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Auto-Analyze Bank SMS", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Tracks BookMyShow BOGO, Swiggy caps & statement dates", color = TextSecondary, fontSize = 11.sp)
                    }

                    Switch(
                        checked = isSmsSyncEnabled,
                        onCheckedChange = { checked ->
                            if (checked) {
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context, Manifest.permission.READ_SMS
                                ) == PackageManager.PERMISSION_GRANTED

                                if (hasPermission) {
                                    smsSyncManager?.setSmsSyncEnabled(true)
                                } else {
                                    smsPermissionLauncher.launch(Manifest.permission.READ_SMS)
                                }
                            } else {
                                smsSyncManager?.setSmsSyncEnabled(false)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = DarkBackground,
                            checkedTrackColor = EmeraldGreen,
                            uncheckedTrackColor = DarkBorder
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Gmail Sync Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Optional Gmail E-Statement Sync", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Reads e-statement totals and reward point summaries", color = TextSecondary, fontSize = 11.sp)
                    }

                    Switch(
                        checked = isGmailSyncEnabled,
                        onCheckedChange = { checked ->
                            if (checked) {
                                gmailSyncManager?.setConnected("user@gmail.com")
                                Toast.makeText(context, "Gmail E-Statement Sync connected", Toast.LENGTH_SHORT).show()
                            } else {
                                gmailSyncManager?.setConnected(null)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = DarkBackground,
                            checkedTrackColor = EmeraldGreen,
                            uncheckedTrackColor = DarkBorder
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            isScanningSms = true
                            val count = smsSyncManager?.scanInbox(30) ?: 0
                            isScanningSms = false
                            Toast.makeText(context, "Scanned & updated $count bank transactions!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkSurfaceVariant,
                        contentColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (isScanningSms) {
                        CircularProgressIndicator(color = EmeraldGreen, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Analyzing Transactions...", fontSize = 12.sp)
                    } else {
                        Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Scan Recent SMS & Update Quotas Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // 2. Intelligent Exhaustion Dashboard (Month / Quarter / Year)
            if (exhaustionStatuses.isNotEmpty()) {
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
                        Text(
                            text = "Exhaustive Offers Status",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ElectricCyan.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "LIVE TRACKER", color = ElectricCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        exhaustionStatuses.take(6).forEach { status ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DarkSurfaceVariant)
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "[${status.window.label}] ",
                                                color = AmberGold,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = status.perkTitle,
                                                color = TextPrimary,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Text(text = status.cardName, color = TextSecondary, fontSize = 11.sp)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (status.isFullyExhausted) Color(0xFFEF5350).copy(alpha = 0.2f) else EmeraldGreen.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (status.isFullyExhausted) "EXHAUSTED 🔒" else "${status.remainingValue.toInt()} ${status.unit} LEFT",
                                            color = if (status.isFullyExhausted) Color(0xFFEF5350) else EmeraldGreen,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(text = "• ${status.sourceTrigger}", color = TextSecondary, fontSize = 11.sp)
                                Text(text = "• ${status.resetDateNotice}", color = TextTertiary, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            // 3. Dynamic Catalog Sync Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CloudSync, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Dynamic Remote Sync",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Allows PerkLens to fetch new bank offers, updated card details, new card art, and brand logos live without needing an app release or repository update.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Catalog Sync Endpoint URL:", color = TextTertiary, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = syncUrlText,
                    onValueChange = {
                        syncUrlText = it
                        syncManager.setSyncUrl(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = EmeraldGreen,
                        unfocusedBorderColor = DarkBorder,
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant
                    ),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Last Synced: ${syncManager.getLastSyncTime()}", color = TextSecondary, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            val success = syncManager.syncLatestCatalog(syncUrlText)
                            if (success) {
                                cardRepo.reloadCatalog()
                                Toast.makeText(context, "Catalog updated successfully!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Sync failed. Check URL or internet.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreen,
                        contentColor = DarkBackground
                    ),
                    shape = RoundedCornerShape(10.dp),
                    enabled = syncState !is SyncState.Syncing
                ) {
                    if (syncState is SyncState.Syncing) {
                        CircularProgressIndicator(color = DarkBackground, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Syncing Catalog...", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sync Latest Cards & Offers Now", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // 4. Recommendation Preferences (Suggest LTF Only)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Tune, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Recommendation Preferences", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Suggest Lifetime Free (LTF) Cards Only", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "Hides all cards with annual or joining fees from AI suggestions", color = TextSecondary, fontSize = 11.sp)
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

            // 5. AI Provider Integration (ThreatLens Style)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AmberGold, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "AI Integration & Model", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Select your preferred AI engine for card analysis and portfolio verdicts.", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(12.dp))

                AiProvider.values().forEach { provider ->
                    val isSelected = currentProvider == provider
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) DarkSurfaceVariant else Color.Transparent)
                            .border(if (isSelected) 1.dp else 0.dp, EmeraldGreen, RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { aiSettings.setProvider(provider) },
                            colors = RadioButtonDefaults.colors(selectedColor = EmeraldGreen)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = provider.displayName, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = provider.badge, color = if (provider == AiProvider.GEMINI_FREE) EmeraldGreen else ElectricCyan, fontSize = 11.sp)
                        }
                    }
                }

                if (currentProvider in listOf(AiProvider.GEMINI_KEY, AiProvider.OPENAI, AiProvider.CLAUDE, AiProvider.GROQ)) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(text = "Enter ${currentProvider.displayName} API Key:", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = apiKeyText,
                        onValueChange = {
                            apiKeyText = it
                            aiSettings.setApiKey(currentProvider, it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (isKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isKeyVisible = !isKeyVisible }) {
                                Icon(
                                    imageVector = if (isKeyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = TextSecondary
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = EmeraldGreen,
                            unfocusedBorderColor = DarkBorder,
                            focusedContainerColor = DarkSurfaceVariant,
                            unfocusedContainerColor = DarkSurfaceVariant
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }
            }
        }
    }
}
