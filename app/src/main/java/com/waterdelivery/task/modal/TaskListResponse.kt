package com.waterdelivery.task.modal

import java.io.Serializable

data class TaskListResponse(
    val authorization: Boolean,
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
):Serializable {
    data class Data(
        val Order_List: List<Order>,
        val current_date: String
    ):Serializable  {
        data class Order(
            val DATEFORMAT: String,
            val address: Address,
            val address_id: String,
            val amt_tax: String,
            val amt_without_tax: String,
            val attributes_id: String,
            val attributes_name: String,
            val brand_name: String,
            val category_id: String,
            val category_name: String,
            var delever_status: String,
            val delivery_time: String,
            val discount: String,
            val end_date: Any,
            val image: String,
            val name: String,
            val order_id: String,
            val product: List<Product>,
            val product_details: String,
            val product_id: String,
            val product_name: String,
            val profile: Any,
            val quantity: String,
            val selling_price: String,
            val start_date: Any,
            val status: String,
            val stock_id: String,
            val stock_name: String,
            val total_amt: String,
            val unit_number: String,
            val unit_price: String,
            val latitude:String,
            val longitude:String
        ):Serializable  {
            data class Address(
                val address: String,
                val city: String,
                val country: Any,
                val customer_id: String,
                val district: String,
                val email: String,
                val first_name: String,
                val id: String,
                val last_name: String,
                val latitude: String,
                val location_address: String,
                val longitude: String,
                val phone_no: String,
                val postcode: String,
                val state: String
            ):Serializable

            data class Product(
                val amount: String,
                val id: String,
                val order_id: String,
                val product_details: String,
                val product_id: String,
                val product_image: String,
                val product_name: String,
                val quantity: String,
                val unit_measure: String
            ):Serializable
        }
    }
}