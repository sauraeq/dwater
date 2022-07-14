package com.waterdelivery.Cart

data class CartCheckoutResponse(
    val authorization: Boolean,
    val `data`: Data,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val amount: String,
        val walletBalance: String,
        val coupon_amount: String,
        val grandtotal: String,
        val product: List<Product>,
        val vat: String
    ) {
        data class Product(
            val cart_id: String,
            val cart_quantity: String,
            val discount: String,
            val image: String,
            val price: String,
            val product_details: String,
            val product_id: String,
            val product_name: String,
            val total_price: String
        )
    }
}