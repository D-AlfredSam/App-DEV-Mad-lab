package com.example.supermarket.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "customer", // "customer" or "admin"
    val address: Address? = null
)

data class Address(
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val pincode: String = ""
)
