package com.waterdelivery

import java.io.Serializable

data class ReturndataResponse(
    val authorization: Boolean,
    val `data`: List<Data>,
    val driver_details: DriverDetails,
    val message: String,
    val status: Boolean
):Serializable{
    data class Data(
        val date: String,
        val returnCustomerList: List<ReturnCustomer>
    ):Serializable {
        data class ReturnCustomer(
            val DATEFORMAT: String,
            val TIMEFORMAT: String,
            val profile: String,
            val mobile_number: String,
            val name: String,
            val product_type: String,
            val quantity: String,
            val remark: String,
            val return_id: String,
            val return_type: String,
            val serial_number: String
        ):Serializable
    }

    data class DriverDetails(
        val address: String,
        val id: String,
        val mobile_number: String,
        val name: String,
        val profile: String
    ):Serializable
}