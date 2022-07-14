package com.waterdelivery.store.model

data class OrderlistResponse(
    val authorization: Boolean,
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val attributes_id: String,
        val attributes_name: String,
        val brand_name: String,
        val category_id: String,
        val category_name: String,
        val delever_status: String,
        val delivery_time: String,
        val discount: String,
        val end_date: Any,
        val image: String,
        val order_id: String,
        val product_details: String,
        val product_id: String,
        val product_name: String,
        val quantity: String,
        val selling_price: String,
        val start_date: Any,
        val status: String,
        val stock_id: String,
        val stock_name: String,
        val total_amt: String,
        val unit_number: String,
        val unit_price: String
    )
}