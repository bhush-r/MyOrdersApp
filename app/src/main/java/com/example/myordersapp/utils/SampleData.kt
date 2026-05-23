package com.example.myordersapp.utils

import com.example.myordersapp.model.Order
import com.example.myordersapp.model.OrderStatus

object SampleData {
    fun getOrders(): List<Order> {
        return listOf(
            Order(
                id = "#ORD12345",
                vehicleType = "Four Wheeler",
                dateTime = "05 Feb, 4:46 PM",
                pickupLocation = "741, Gumanwara",
                dropLocation = "00, Main Rd, Shivaji Nagar, Jhansi, Uttar Pradesh 284001, India",
                price = "₹ 229.0",
                status = OrderStatus.CANCELLED
            ),
            Order(
                id = "#ORD12346",
                vehicleType = "Four Wheeler",
                dateTime = "05 Feb, 4:46 PM",
                pickupLocation = "741, Gumanwara",
                dropLocation = "00, Main Rd, Shivaji Nagar, Jhansi, Uttar Pradesh 284001, India",
                price = "₹ 229.0",
                status = OrderStatus.CANCELLED
            ),
            Order(
                id = "#ORD12347",
                vehicleType = "Four Wheeler",
                dateTime = "05 Feb, 4:46 PM",
                pickupLocation = "332, Gumanwara",
                dropLocation = "GC72+GGV, Kamrari, Madhya Pradesh 475661, India",
                price = "₹ 1515.0",
                status = OrderStatus.CANCELLED
            ),
            Order(
                id = "#ORD12348",
                vehicleType = "Four Wheeler",
                dateTime = "05 Feb, 4:46 PM",
                pickupLocation = "332, Gumanwara",
                dropLocation = "GC72+GGV, Kamrari, Madhya Pradesh 475661, India",
                price = "₹ 1634.0",
                status = OrderStatus.CANCELLED
            ),
            Order(
                id = "#ORD12349",
                vehicleType = "Four Wheeler",
                dateTime = "06 Feb, 2:30 PM",
                pickupLocation = "123, MG Road",
                dropLocation = "456, Park Street, Kolkata, West Bengal 700016, India",
                price = "₹ 450.0",
                status = OrderStatus.COMPLETED
            ),
            Order(
                id = "#ORD12350",
                vehicleType = "Four Wheeler",
                dateTime = "07 Feb, 10:15 AM",
                pickupLocation = "789, Anna Salai",
                dropLocation = "321, T Nagar, Chennai, Tamil Nadu 600017, India",
                price = "₹ 380.0",
                status = OrderStatus.COMPLETED
            )
        )
    }
}