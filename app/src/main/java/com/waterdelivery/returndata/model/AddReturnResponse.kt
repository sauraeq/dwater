package com.waterdelivery.returndata.model

data class AddReturnResponse(
    val authorization: Boolean,
    val `data`: List<Any>,
    val message: String,
    val status: Boolean
)