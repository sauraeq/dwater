package com.waterdelivery.user.modal

data class UserProfileUpdate(
    val authorization: Boolean,
    val `data`: List<Any>,
    val message: String,
    val status: Boolean
)