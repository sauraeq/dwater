package com.waterdelivery.returndata.model

data class ListResponse(
    val authorization: Boolean,
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)