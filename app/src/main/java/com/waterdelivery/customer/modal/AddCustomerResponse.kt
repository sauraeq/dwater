package com.waterdelivery.customer.modal

data class AddCustomerResponse(
    val authorization: Boolean,
    val `data`: List<Any>,
    val message: String,
    val status: Boolean
)