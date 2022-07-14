package com.waterdelivery.Checkout

data class WalletResponse(
    val authorization: Boolean,
    val `data`: Data,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val id: String,
        val wallet_amount: String
    )
}