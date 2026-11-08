package io.perklens.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import io.perklens.core.ai.AiProvider
import io.perklens.core.ai.AiSettingsRepository
import io.perklens.core.ai.CardAiVerdictEngine
import io.perklens.core.diagnostics.AppDiagnosticsLogManager
import io.perklens.core.diagnostics.DiagnosticLogEntry
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
    diagnosticsLogger: AppDiagnosticsLogManager? = null,
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

    var isTestingAi by remember { mutableStateOf(false) }
    var aiTestResult by remember { mutableStateOf<Pair<Boolean, String>?>(null) }

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
                title = {
                    Column {
                        Text("Settings & Multi-Engine AI", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Configure AI providers, OTA sources & local vault", fontSize = 11.sp, color = TextSecondary)
                    }
                },
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
            // =================================================================
            // 1. MULTI-ENGINE AI INTEGRATION HUB (SECTION 1)
            // =================================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.5.dp, DarkBorderNeon, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Multi-Engine AI Integration",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Select active model for card advice & verdicts",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Zero-KYC 🔒",
                            color = NeonGreenBright,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Provider Selection Cards
                AiProvider.values().forEach { provider ->
                    val isSelected = currentProvider == provider
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) DarkSurfaceVariant else Color.Transparent)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) NeonGreen else DarkBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                aiSettings.setProvider(provider)
                                apiKeyText = aiSettings.getApiKey(provider)
                                aiTestResult = null
                            }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                aiSettings.setProvider(provider)
                                apiKeyText = aiSettings.getApiKey(provider)
                                aiTestResult = null
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = NeonGreen,
                                unselectedColor = TextTertiary
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = provider.displayName,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                            )
                            Text(
                                text = provider.badge,
                                color = if (isSelected) NeonGreenBright else TextTertiary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // API Key Input for Custom Key Providers
                if (currentProvider in listOf(AiProvider.GEMINI_KEY, AiProvider.OPENAI, AiProvider.CLAUDE, AiProvider.GROQ)) {
                    Spacer(modifier = Modifier.height(14.dp))
                    val providerLabel = currentProvider.displayName.substringBefore('(').trim()
                    Text(
                        text = "Enter $providerLabel API Key:",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = apiKeyText,
                        onValueChange = {
                            apiKeyText = it
                            aiSettings.setApiKey(currentProvider, it)
                            aiTestResult = null
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
                        placeholder = { Text("Paste your API key here...", color = TextTertiary, fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = DarkBorder,
                            focusedContainerColor = DarkSurfaceVariant,
                            unfocusedContainerColor = DarkSurfaceVariant
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Test Connection Button
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isTestingAi = true
                                aiTestResult = CardAiVerdictEngine.testConnection(currentProvider, apiKeyText)
                                isTestingAi = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricCyan,
                            contentColor = DarkBackground
                        ),
                        shape = RoundedCornerShape(10.dp),
                        enabled = !isTestingAi
                    ) {
                        if (isTestingAi) {
                            CircularProgressIndicator(color = DarkBackground, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Testing Connection...", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Test AI Connection & Check Latency", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Test Result Display
                    aiTestResult?.let { (success, msg) ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (success) NeonGreen.copy(alpha = 0.15f) else Color(0xFFEF5350).copy(alpha = 0.15f))
                                .border(1.dp, if (success) NeonGreen else Color(0xFFEF5350), RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (success) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = if (success) NeonGreenBright else Color(0xFFEF5350),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = msg,
                                color = if (success) NeonGreenBright else Color(0xFFEF5350),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Zero-KYC Key Security Reassurance
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkBackground.copy(alpha = 0.6f))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Keys are saved strictly on-device in Context.MODE_PRIVATE. Zero telemetry, zero external logging.",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Suggest LTF Only Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Suggest Lifetime Free (LTF) Cards Only", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "Filter out cards with annual or joining fees from AI advice", color = TextSecondary, fontSize = 11.sp)
                    }

                    Switch(
                        checked = suggestLtfOnly,
                        onCheckedChange = { aiSettings.setSuggestLtfOnly(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = DarkBackground,
                            checkedTrackColor = NeonGreen,
                            uncheckedTrackColor = DarkBorder
                        )
                    )
                }
            }

            // =================================================================
            // 2. DYNAMIC CATALOG REMOTE SYNC & RELIABLE UPDATE SOURCES (SECTION 2)
            // =================================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CloudSync, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Dynamic Remote Catalog Sync",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${cardRepo.getAllCards().size} cards & privileges in local database",
                            color = NeonGreenBright,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "PerkLens automatically updates bank card perks, lounge access criteria, and devaluations live without requiring an APK update.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Reliable Sources Notice
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkBackground.copy(alpha = 0.6f))
                        .padding(10.dp)
                ) {
                    Text(text = "📡 Reliable Catalog Sources:", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "• Official Bank Tariff Sheets (BoB, HDFC, SBI, ICICI, Axis, Amex, IDFC)", color = TextSecondary, fontSize = 10.sp)
                    Text(text = "• TechnoFino & CardExpert Community Verifications", color = TextSecondary, fontSize = 10.sp)
                    Text(text = "• RBI Master Directions on Credit & Debit Card Issuance", color = TextSecondary, fontSize = 10.sp)
                    Text(text = "• Instant Over-The-Air (OTA) GitHub Sync CDN", color = TextSecondary, fontSize = 10.sp)
                }

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
                        focusedBorderColor = ElectricCyan,
                        unfocusedBorderColor = DarkBorder,
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant
                    ),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Last Synced: ${syncManager.getLastSyncTime()}", color = TextSecondary, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            val success = syncManager.syncLatestCatalog(syncUrlText)
                            if (success) {
                                cardRepo.reloadCatalog()
                                Toast.makeText(context, "Catalog updated! ${cardRepo.getAllCards().size} cards available.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Sync failed. Checking offline backup.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonGreen,
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

            // =================================================================
            // 3. AUTOMATED SMS & STATEMENT MULTI-HORIZON TRACKING (SECTION 3)
            // =================================================================
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
                        text = "Automated Spend & Quota Tracker",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Parses bank transaction notifications on-device to calculate consumed cinema tickets, cashback caps, and annual fee waivers.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // SMS Sync Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Auto-Analyze Bank SMS", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Tracks BookMyShow BOGO, Swiggy caps & resets", color = TextSecondary, fontSize = 11.sp)
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
                            checkedTrackColor = NeonGreen,
                            uncheckedTrackColor = DarkBorder
                        )
                    )
                }

                if (isSmsSyncEnabled) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isScanningSms = true
                                val count = smsSyncManager?.scanInbox(daysBack = 30) ?: 0
                                isScanningSms = false
                                Toast.makeText(context, "Scanned $count recent transactions", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkSurfaceVariant,
                            contentColor = NeonGreenBright
                        ),
                        shape = RoundedCornerShape(10.dp),
                        enabled = !isScanningSms
                    ) {
                        if (isScanningSms) {
                            CircularProgressIndicator(color = NeonGreenBright, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Analyzing SMS Inbox...", fontSize = 12.sp)
                        } else {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Scan Recent Bank SMS Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // =================================================================
            // 4. DIAGNOSTICS & ERROR LOG EXPORT (SECTION 4)
            // =================================================================
            val logs by (diagnosticsLogger?.logs?.collectAsState() ?: remember { mutableStateOf(emptyList()) })

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.dp, if (logs.isNotEmpty()) Color(0xFFEF5350).copy(alpha = 0.5f) else DarkBorderNeon, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.BugReport,
                            contentDescription = null,
                            tint = if (logs.isNotEmpty()) Color(0xFFEF5350) else NeonGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Diagnostics & Error Logs",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (logs.isNotEmpty()) Color(0xFFEF5350).copy(alpha = 0.2f) else NeonGreen.copy(alpha = 0.15f))
                            .border(0.5.dp, if (logs.isNotEmpty()) Color(0xFFEF5350) else NeonGreen, RoundedCornerShape(8.dp))
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (logs.isNotEmpty()) "${logs.size} Traces" else "0 Errors ✓",
                            color = if (logs.isNotEmpty()) Color(0xFFFF8A80) else NeonGreenBright,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Track failed catalog updates, network timeouts, or AI engine exceptions. Export full formatted incident reports so we can fix issues and build a better app.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Error Entries Preview
                if (logs.isEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonGreen.copy(alpha = 0.08f))
                            .border(0.5.dp, NeonGreen.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "All systems operational. Zero failed updates recorded.",
                            color = NeonGreenBright,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceVariant)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Recent Incident Traces (Most Recent First):",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        logs.take(3).forEach { entry ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (entry.level == "ERROR") Color(0xFFEF5350) else Color(0xFFFFB300))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(text = entry.tag, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = entry.timestamp, color = TextTertiary, fontSize = 10.sp)
                                }
                                Text(
                                    text = entry.message,
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 2
                                )
                            }
                            HorizontalDivider(color = DarkBorder, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 2.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            diagnosticsLogger?.exportAndShareReport(context, cardRepo.getAllCards().size)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricCyan,
                            contentColor = DarkBackground
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export Error Logs", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    if (logs.isNotEmpty()) {
                        OutlinedButton(
                            onClick = {
                                diagnosticsLogger?.clearLogs()
                                Toast.makeText(context, "Error logs cleared", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.width(110.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF5350)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF5350).copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Clear", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
