package com.waterdelivery.orderdelivery

data class OrderDiliveryResponse(
    val authorization: Boolean,
    val message: String,
    val status: Boolean
)