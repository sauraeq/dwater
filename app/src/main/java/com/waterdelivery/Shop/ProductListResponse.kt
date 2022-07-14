package com.waterdelivery.Shop

data class ProductListResponse(
    val authorization: Boolean,
    val count: Int,
    val `data`: List<Data>,
    val message: String,
    val next_page: Boolean,
    val status: Boolean,
    val total_count: Int
) {
    data class Data(
        val brand_name: String,
        val category_id: String,
        val category_name: String,
        val discount: String,
        val image: String,
        val product_details: String,
        val product_id: String,
        val product_name: String,
        val quantity: String,
        val selling_price: String,
        val unit_number: String,
        val unit_price: String
    )
}