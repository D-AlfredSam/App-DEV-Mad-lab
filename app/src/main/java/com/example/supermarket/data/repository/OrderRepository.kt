package com.example.supermarket.data.repository

import com.example.supermarket.data.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class OrderRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun placeOrder(order: Order): String {
        val doc = firestore.collection("orders").document()
        val newOrder = order.copy(id = doc.id)
        doc.set(newOrder).await()
        return doc.id
    }

    fun getAllOrders(): Flow<List<Order>> = callbackFlow {
        val subscription = firestore.collection("orders")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    trySend(snapshot.toObjects(Order::class.java))
                }
            }
        awaitClose { subscription.remove() }
    }

    fun getCustomerOrders(customerId: String): Flow<List<Order>> = callbackFlow {
        val subscription = firestore.collection("orders")
            .whereEqualTo("customerId", customerId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    trySend(snapshot.toObjects(Order::class.java))
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun updateOrderStatus(orderId: String, status: String) {
        firestore.collection("orders").document(orderId).update("status", status).await()
    }
}
