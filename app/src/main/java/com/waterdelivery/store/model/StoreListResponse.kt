package com.waterdelivery.store.model

import java.io.Serializable

data class StoreListResponse(
    val authorization: Boolean,
    val `data`: List<Data>,
    val driver_details: DriverDetails,
    val message: String,
    val status: Boolean
) : Serializable {
    data class Data(
        val address: String,
        val base_price: String,
        val comments: String,
        val created_date: String,
        var driver_acceptance: String,
        val driver_id: String,
        val id: String,
        val image: String,
        val quanity: String,
        val status: String,
        val store_name: String,

        val new_quantity:String,
        val quantity:String


    ) : Serializable

    data class DriverDetails(
        val address: String,
        val id: String,
        val mobile_number: String,
        val name: String,
        val profile: String
    ) : Serializable
}