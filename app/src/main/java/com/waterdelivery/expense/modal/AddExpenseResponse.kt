package com.waterdelivery.expense.modal

data class AddExpenseResponse(
    val authorization: Boolean,
    val `data`: List<Any>,
    val message: String,
    val status: Boolean
)