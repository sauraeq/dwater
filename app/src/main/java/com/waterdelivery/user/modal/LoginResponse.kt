package com.waterdelivery.user.modal

data class LoginResponse(
    val authorization: Boolean,
    val `data`: Data,
    val message: String,
    val status: Boolean,
    val token: String
)