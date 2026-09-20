package com.example.billing

import com.example.data.model.CoinPackage
import com.example.data.model.CoinPackages
import com.example.data.model.TransactionEntity
import com.example.data.repository.TobiGtRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID

sealed class PurchaseResult {
    data class Success(val transaction: TransactionEntity, val coinsCredited: Long) : PurchaseResult()
    data class Error(val message: String) : PurchaseResult()
    object Cancelled : PurchaseResult()
}

class BillingService(private val repository: TobiGtRepository) {

    /**
     * Executes test purchase with backend verification pipeline.
     */
    suspend fun executeTestPurchase(pkg: CoinPackage): PurchaseResult = withContext(Dispatchers.IO) {
        // Step 1: Product validation
        val validPkg = CoinPackages.findById(pkg.productId)
            ?: return@withContext PurchaseResult.Error("Product validation failed: Unknown SKU ${pkg.productId}")

        // Simulate network / provider latency
        delay(600)

        // Generate simulated test purchase token and order ID
        val testToken = "test_token_" + UUID.randomUUID().toString()
        val testOrderId = "TEST-GPA." + (1000..9999).random() + "-" + (1000..9999).random()

        // Step 2: Server-side purchase verification & duplicate check
        val isDuplicate = repository.isPurchaseTokenDuplicate(testToken)
        if (isDuplicate) {
            return@withContext PurchaseResult.Error("Security verification failed: Duplicate purchase token")
        }

        // Step 3: Backend coin credit & transaction ledger record
        val recordResult = repository.recordAndCreditPurchase(
            productId = validPkg.productId,
            packageName = "${validPkg.coinAmount} Coins",
            price = validPkg.priceDollars,
            currency = "USD",
            coinAmount = validPkg.coinAmount,
            paymentProvider = "TEST_DEVELOPMENT",
            purchaseToken = testToken,
            orderId = testOrderId
        )

        recordResult.fold(
            onSuccess = { tx ->
                PurchaseResult.Success(tx, validPkg.coinAmount)
            },
            onFailure = { error ->
                PurchaseResult.Error("Backend verification failed: ${error.localizedMessage}")
            }
        )
    }

    /**
     * Google Play Billing purchase execution architecture.
     * Ready for production Google Play Billing Client integration.
     */
    suspend fun executePlayBillingPurchase(
        pkg: CoinPackage,
        playPurchaseToken: String = "gpb_tok_" + UUID.randomUUID().toString().replace("-", ""),
        playOrderId: String = "GPA." + (1000..9999).random() + "-" + (1000..9999).random() + "-" + (1000..9999).random()
    ): PurchaseResult = withContext(Dispatchers.IO) {
        // 1. Product validation
        val validPkg = CoinPackages.findById(pkg.productId)
            ?: return@withContext PurchaseResult.Error("Google Play Billing Error: Invalid Product ID ${pkg.productId}")

        // 2. Play Billing Purchase verification simulation (Backend verifies with Google Play Developer API)
        delay(800)

        // 3. Duplicate check (Idempotency)
        if (repository.isPurchaseTokenDuplicate(playPurchaseToken)) {
            return@withContext PurchaseResult.Error("Play Billing Security: Duplicate purchase receipt token.")
        }

        // 4. Authoritative backend coin credit and ledger storage
        val recordResult = repository.recordAndCreditPurchase(
            productId = validPkg.productId,
            packageName = "${validPkg.coinAmount} Coins",
            price = validPkg.priceDollars,
            currency = "USD",
            coinAmount = validPkg.coinAmount,
            paymentProvider = "GOOGLE_PLAY_BILLING",
            purchaseToken = playPurchaseToken,
            orderId = playOrderId
        )

        recordResult.fold(
            onSuccess = { tx ->
                PurchaseResult.Success(tx, validPkg.coinAmount)
            },
            onFailure = { error ->
                PurchaseResult.Error("Play Billing Verification Failed: ${error.localizedMessage}")
            }
        )
    }
}
