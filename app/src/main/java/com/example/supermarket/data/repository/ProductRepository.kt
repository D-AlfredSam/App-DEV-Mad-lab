package com.example.supermarket.data.repository

import com.example.supermarket.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProductRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getProducts(): Flow<List<Product>> = callbackFlow {
        val subscription = firestore.collection("products")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val products = snapshot.toObjects(Product::class.java)
                    trySend(products)
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun addProduct(product: Product) {
        val doc = firestore.collection("products").document()
        val newProduct = product.copy(id = doc.id)
        doc.set(newProduct).await()
    }

    suspend fun updateProduct(product: Product) {
        firestore.collection("products").document(product.id).set(product).await()
    }

    suspend fun deleteProduct(productId: String) {
        firestore.collection("products").document(productId).delete().await()
    }

    suspend fun updateStock(productId: String, quantityChange: Int) {
        val docRef = firestore.collection("products").document(productId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val currentStock = snapshot.getLong("stock") ?: 0
            transaction.update(docRef, "stock", currentStock + quantityChange)
        }.await()
    }
}
