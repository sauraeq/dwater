package com.waterdelivery.user.modal

data class GetProfileResponse(
    val authorization: Boolean,
    val `data`: Data,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val address: String,
        val helper_name: String,
        val vehicle_number_plate: String,
        val business_id: String,
        val business_name: String,
        val country: String,
        val customer_id: String,
        val dob: String,
        val email: String,
        val gender: String,
        val latitude: String,
        val longitude: String,
        val mobile_number: String,
        val name: String,
        val profile: String,
        val role_name: String
    )
}