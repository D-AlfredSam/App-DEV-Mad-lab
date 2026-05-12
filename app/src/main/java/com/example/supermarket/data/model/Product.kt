package com.example.supermarket.data.model

data class Product(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val description: String = "",
    val unit: String = "piece" // kg, litre, piece
)
