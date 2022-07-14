package com.waterdelivery.customer.fragement.model

data class SearchResponse(
    val authorization: Boolean,
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val created_date: String,
        val email: String,
        val id: String,
        val name: String,
        val mobile_number:String,
        val wallet_amount:String,
        val profile:String,
        val phone: String,
        val status: String
    )
}