package com.example.myordersapp.model

data class Order(
    val id: String,
    val vehicleType: String,
    val dateTime: String,
    val pickupLocation: String,
    val dropLocation: String,
    val price: String,
    val status: OrderStatus
)

enum class OrderStatus {
    ALL,
    COMPLETED,
    CANCELLED,
    BOOKED_AGAIN
}