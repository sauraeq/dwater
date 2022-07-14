package com.waterdelivery.userfinal

data class ActivityRespons(
    val authorization: Boolean,
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val current_date: String,
        val driver_activity: DriverActivity
    ) {
        data class DriverActivity(
            val final_net_available: String,
            val credit_sale: String,
            val total_cash_sale: String,
            val total_earning: String,
            val total_expense: String,
            val total_topup: String,
            val cash_sale: String,
            val wallet_sale: String
        )
    }
}