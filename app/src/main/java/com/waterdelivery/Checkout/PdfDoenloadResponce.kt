package com.waterdelivery.Checkout

data class PdfDoenloadResponce(
    val authorization: Boolean,
    val `data`: Data,
    val message: String,
    val status: Boolean
)