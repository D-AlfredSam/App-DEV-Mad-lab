package com.example.supermarket.data.repository

import com.example.supermarket.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun getCurrentUser() = auth.currentUser

    suspend fun getUserDetails(uid: String): User? {
        return try {
            firestore.collection("users").document(uid).get().await().toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveUserDetails(user: User) {
        firestore.collection("users").document(user.uid).set(user).await()
    }
    
    fun logout() {
        auth.signOut()
    }
}
