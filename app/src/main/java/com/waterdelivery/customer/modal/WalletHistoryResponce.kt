package com.waterdelivery.customer.modal

data class WalletHistoryResponce(
    val authorization: Boolean,
    val `data`: ArrayList<Data>,
    val message: String,
    val status: Boolean,
    val wallet_balance: String
){
    data class Data(
        val address: String,
        val amount: String,
        val created_date: String,
        val name: String,
        val profile: Any
    )
}