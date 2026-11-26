package com.gghez.game2048.inapp

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*

class BillingManager(
    private val context: Context,
    private val onPurchased: () -> Unit
) {
    private val premiumProducts = listOf(
        "premium_basic",
        "premium_standard",
        "premium_plus",
        "premium_pro",
        "premium_lifetime"
    )


    private val billingClient = BillingClient.newBuilder(context)
        .setListener { _, purchases ->
            purchases?.forEach { purchase ->
                if (purchase.products.any { it in premiumProducts }) {
                    savePremium()
                    onPurchased()
                }
            }
        }

        .enablePendingPurchases()
        .build()

    fun start() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {}
            override fun onBillingServiceDisconnected() {}
        })
    }

    fun buy(activity: Activity, productId: String) {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            ).build()

        billingClient.queryProductDetailsAsync(params) { _, productDetailsList ->
            if (productDetailsList.isNotEmpty()) {
                val flowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(
                        listOf(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetailsList[0])
                                .build()
                        )
                    ).build()

                billingClient.launchBillingFlow(activity, flowParams)
            }
        }
    }


    private fun savePremium() {
        context.getSharedPreferences("app", Context.MODE_PRIVATE)
            .edit().putBoolean("premium", true).apply()
    }

    fun isPremium(): Boolean {
        return context.getSharedPreferences("app", Context.MODE_PRIVATE)
            .getBoolean("premium", false)
    }
}
