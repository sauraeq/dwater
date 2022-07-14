package com.waterdelivery.store.model

data class StoreAcceptResponse(
    val authorization: Boolean,
    val message: String,
    val status: Boolean
)