package com.waterdelivery.user.modal

data class UpdateProfileResponse(
    val authorization: Boolean,
    val message: String,
    val status: Boolean,
    val data:String
)