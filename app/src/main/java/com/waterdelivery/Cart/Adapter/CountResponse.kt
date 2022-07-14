package com.waterdelivery.Cart.Adapter

data class CountResponse(
    val TotalCount: String,
    val authorization: Boolean,
    val message: String,
    val status: Boolean
)