package com.waterdelivery.customer.fragement.model

import java.io.Serializable

data class AddmoneyResponse(
    val authorization: Boolean,
    val `data`: Data,
    val message: String,
    val status: Boolean
):Serializable{
    data class Data(
        val amt: String,
        val amt_after_trans: String,
        val amt_before_trans: String,
        val comment: String,
        val created_by: String,
        val created_date: String,
        val customer_id: String,
        val driver_name: String,
        val id: String,
        val is_active: String,
        val trans_type: String,
        val wallet_id: String,
        val wallet_status: String
    ):Serializable
}