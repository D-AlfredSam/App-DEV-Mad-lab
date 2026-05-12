package com.example.supermarket.data.model

import com.google.firebase.Timestamp

data class Order(
    val id: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val customerAddress: String = "",
    val products: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val paymentMethod: String = "", // "COD" or "UPI"
    val transactionId: String? = null,
    val status: String = "Pending", // Pending, Accepted, Packed, Delivered, Cancelled
    val timestamp: Timestamp = Timestamp.now()
)
