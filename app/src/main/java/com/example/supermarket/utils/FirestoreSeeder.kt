package com.example.supermarket.utils

import com.example.supermarket.data.model.Product
import com.example.supermarket.data.model.Supermarket
import com.example.supermarket.data.repository.ProductRepository
import com.example.supermarket.data.repository.SupermarketRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirestoreSeeder {
    suspend fun seedIfNeeded() {
        val firestore = FirebaseFirestore.getInstance()
        val supermarketRepo = SupermarketRepository()
        val productRepo = ProductRepository()

        // 1. Seed Supermarket Info if missing
        val shop = supermarketRepo.getSupermarketInfo()
        if (shop == null) {
            supermarketRepo.updateSupermarketInfo(
                Supermarket(
                    name = "Fresh Mart Supermarket",
                    address = "123 Market Road, City Center",
                    phone = "9876543210",
                    upiId = "freshmart@okaxis",
                    workingHours = "9:00 AM - 10:00 PM"
                )
            )
        }

        // 2. Seed Products if empty
        val productsSnap = firestore.collection("products").limit(1).get().await()
        if (productsSnap.isEmpty) {
            val sampleProducts = listOf(
                Product(name = "Basmati Rice", category = "Grains", price = 120.0, stock = 50, unit = "kg"),
                Product(name = "Whole Wheat Flour", category = "Atta", price = 450.0, stock = 30, unit = "5kg"),
                Product(name = "Refined Oil", category = "Oil", price = 150.0, stock = 100, unit = "Litre"),
                Product(name = "Sugar", category = "Grocery", price = 45.0, stock = 200, unit = "kg"),
                Product(name = "Milk", category = "Dairy", price = 60.0, stock = 40, unit = "Litre")
            )
            sampleProducts.forEach { productRepo.addProduct(it) }
        }
    }
}
