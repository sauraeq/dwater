package com.waterdelivery.Cart

data class CartListResponse(
    val authorization: Boolean,
    val `data`: List<Data>,
    val message: String,
    val status: Boolean,
    val total_price: String
) {
    data class Data(
        val cart_id: String,
        val cart_quantity: String,
        val discount: String,
        val image: String,
        val price: String,
        val product_details: String,
        val product_id: String,
        val product_name: String,
        val max_quantity: String,
        val total_price: String
    )
}