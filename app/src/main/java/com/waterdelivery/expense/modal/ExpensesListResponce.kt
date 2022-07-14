package com.waterdelivery.expense.modal

data class ExpensesListResponce(
    val authorization: Boolean,
    val `data`: List<Data>,
    val driver_details: DriverDetails,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val amount: String,
        val comment: String,
        val created_date: String,
        val driver_id: String,
        val id: String,
        val image: String,
        val type: String
    )

    data class DriverDetails(
        val driver_address: String,
        val driver_image: String,
        val driver_name: String,
        val driver_total_amount: String
    )
}