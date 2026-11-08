package io.perklens.core.parser

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

data class ParsedBankTransaction(
    val id: String = System.currentTimeMillis().toString(),
    val bank: String,
    val cardLast4: String?,
    val amountInr: Double,
    val merchant: String,
    val timestampStr: String,
    val isStatement: Boolean = false,
    val statementDueDate: String? = null
)

object BankSmsParser {

    private val BANK_SENDER_PATTERNS = listOf(
        "HDFCBK" to "HDFC Bank",
        "SBICRD" to "SBI Card",
        "ICICIB" to "ICICI Bank",
        "AXISBK" to "Axis Bank",
        "AMEXIN" to "American Express",
        "FEDBNK" to "Federal Bank",
        "HSBCIN" to "HSBC",
        "ONECRD" to "OneCard"
    )

    // Regex patterns for transaction amounts and merchants
    private val TXN_PATTERNS = listOf(
        // Pattern 1: "spent Rs 1,499.00 on Swiggy with card ending XX8824"
        Pattern.compile("""(?:spent|debited|txn of)\s*(?:INR|Rs\.?)\s*([0-9,]+(?:\.[0-9]+)?).*?(?:on|at)\s+([A-Za-z0-9\s*.\-_]+?)(?:\s+(?:with|using|on|via)\s+(?:card|xx)\s*([0-9]{4})|\.|$)""", Pattern.CASE_INSENSITIVE),
        // Pattern 2: "Rs 540.00 spent on Card xx1234 at Amazon"
        Pattern.compile("""(?:INR|Rs\.?)\s*([0-9,]+(?:\.[0-9]+)?)\s*spent\s*(?:on|with)\s*(?:card\s*)?(?:xx)?([0-9]{4})?\s*at\s+([A-Za-z0-9\s*.\-_]+)""", Pattern.CASE_INSENSITIVE)
    )

    // Regex for monthly statement SMS
    private val STATEMENT_PATTERN = Pattern.compile(
        """(?:Statement|Total Due|Total Amount Due).*?(?:INR|Rs\.?)\s*([0-9,]+(?:\.[0-9]+)?).*?(?:Due Date|due on|by)\s*([0-9A-Za-z\-/]+)""",
        Pattern.CASE_INSENSITIVE
    )

    fun isBankSender(sender: String): Pair<Boolean, String> {
        val s = sender.uppercase()
        for ((code, name) in BANK_SENDER_PATTERNS) {
            if (s.contains(code)) return Pair(true, name)
        }
        return Pair(false, "Unknown Bank")
    }

    fun parseMessage(sender: String, body: String, dateMillis: Long): ParsedBankTransaction? {
        val (isBank, bankName) = isBankSender(sender)
        if (!isBank) return null

        // Ignore OTP messages completely for security & noise reduction
        val lower = body.lowercase()
        if (lower.contains("otp") || lower.contains("verification code") || lower.contains("do not share")) {
            return null
        }

        val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(dateMillis))

        // Check for Statement generation SMS
        val stmtMatcher = STATEMENT_PATTERN.matcher(body)
        if (stmtMatcher.find()) {
            val amountRaw = stmtMatcher.group(1)?.replace(",", "")?.toDoubleOrNull() ?: 0.0
            val dueDate = stmtMatcher.group(2)?.trim()
            return ParsedBankTransaction(
                bank = bankName,
                cardLast4 = extractCardLast4(body),
                amountInr = amountRaw,
                merchant = "Monthly Statement",
                timestampStr = dateStr,
                isStatement = true,
                statementDueDate = dueDate
            )
        }

        // Check for normal spend transactions
        for (pattern in TXN_PATTERNS) {
            val matcher = pattern.matcher(body)
            if (matcher.find()) {
                val amountRaw = matcher.group(1)?.replace(",", "")?.toDoubleOrNull() ?: continue
                var merchantRaw = matcher.group(2)?.trim() ?: "Merchant"
                // Clean up merchant string
                if (merchantRaw.length > 25) merchantRaw = merchantRaw.take(25)

                val cardLast4 = extractCardLast4(body)

                return ParsedBankTransaction(
                    bank = bankName,
                    cardLast4 = cardLast4,
                    amountInr = amountRaw,
                    merchant = cleanMerchantName(merchantRaw),
                    timestampStr = dateStr,
                    isStatement = false
                )
            }
        }

        return null
    }

    private fun extractCardLast4(body: String): String? {
        val p = Pattern.compile("""(?:card|ending|xx)\s*(?:no\.?)?\s*([0-9]{4})""", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(body)
        return if (m.find()) m.group(1) else null
    }

    private fun cleanMerchantName(raw: String): String {
        val cleaned = raw.replace(Regex("""(?i)\b(inr|rs|on|at|via|with|using|pvt|ltd|co)\b"""), "").trim()
        return when {
            cleaned.contains("swiggy", ignoreCase = true) -> "Swiggy"
            cleaned.contains("zomato", ignoreCase = true) -> "Zomato"
            cleaned.contains("amazon", ignoreCase = true) -> "Amazon"
            cleaned.contains("flipkart", ignoreCase = true) -> "Flipkart"
            cleaned.contains("blinkit", ignoreCase = true) -> "Blinkit"
            cleaned.contains("uber", ignoreCase = true) -> "Uber"
            cleaned.contains("ola", ignoreCase = true) -> "Ola"
            cleaned.contains("makemytrip", ignoreCase = true) -> "MakeMyTrip"
            cleaned.contains("bookmyshow", ignoreCase = true) -> "BookMyShow"
            cleaned.isNotBlank() -> cleaned
            else -> "Merchant POS"
        }
    }
}
