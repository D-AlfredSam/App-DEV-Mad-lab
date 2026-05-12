package com.example.supermarket.data.model

data class CartItem(
    val productId: String = "",
    val name: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0,
    val unit: String = ""
)
