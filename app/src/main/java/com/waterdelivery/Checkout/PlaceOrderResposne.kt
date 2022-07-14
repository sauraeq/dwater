package com.waterdelivery.Checkout

import java.io.Serializable

data class PlaceOrderResposne (
    val authorization: Boolean,
    val `data`: Data,
    val message: String,
    val status: Boolean
):Serializable {
    data class Data(
        val result: Result,
        val success: Boolean
    ) : Serializable {
        data class Result(
            val TotalCartamount: String,
            val vat: String,
            val customer_id: String,
            val orderId: Int,
            val referenceId: String,
            val customer_name: String,
            val customer_trn: String,
            val product_list: List<Productlist>,
            val wallet_amount: String,
            val amount_without_vat: String,
        ) : Serializable {
            data class Productlist(
                val amount: String,
                val order_id: Int,
                val product_id: String,
                val product_name: String,
                val quantity: String,
                val vat: String
            ):Serializable
        }

    }
}
