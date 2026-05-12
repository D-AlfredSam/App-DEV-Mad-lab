package com.example.supermarket.utils

import com.example.supermarket.data.model.CartItem
import com.example.supermarket.data.model.Product

object CartManager {
    private val items = mutableMapOf<String, CartItem>()

    fun addItem(product: Product) {
        val existing = items[product.id]
        if (existing != null) {
            items[product.id] = existing.copy(quantity = existing.quantity + 1)
        } else {
            items[product.id] = CartItem(
                productId = product.id,
                name = product.name,
                quantity = 1,
                price = product.price,
                unit = product.unit
            )
        }
    }

    fun removeItem(productId: String) {
        items.remove(productId)
    }

    fun updateQuantity(productId: String, quantity: Int) {
        val existing = items[productId]
        if (existing != null) {
            if (quantity <= 0) items.remove(productId)
            else items[productId] = existing.copy(quantity = quantity)
        }
    }

    fun getItems(): List<CartItem> = items.values.toList()

    fun getTotal(): Double = items.values.sumOf { it.price * it.quantity }

    fun clear() {
        items.clear()
    }
}
