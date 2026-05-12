package com.example.supermarket.data.repository

import com.example.supermarket.data.model.Supermarket
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SupermarketRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun getSupermarketInfo(): Supermarket? {
        return try {
            firestore.collection("supermarket").document("info")
                .get().await().toObject(Supermarket::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateSupermarketInfo(info: Supermarket) {
        firestore.collection("supermarket").document("info").set(info).await()
    }
}
