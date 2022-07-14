package com.waterdelivery.notification

data class NotificationResponse(
    val authorization: Boolean,
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
) {
    data class Data(
        val created_date: String,
        val id: String,
        val message: String,
        val title: String
    )
}