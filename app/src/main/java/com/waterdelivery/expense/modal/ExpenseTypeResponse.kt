package com.waterdelivery.expense.modal

data class ExpenseTypeResponse(
    val authorization: Boolean,
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val id: String,
        val name: String,
        val status: String
    )
}