package com.example.supermarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supermarket.data.model.User
import com.example.supermarket.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _userState = MutableLiveData<User?>()
    val userState: LiveData<User?> = _userState

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun checkUser() {
        val current = repository.getCurrentUser()
        if (current != null) {
            fetchUserDetails(current.uid)
        } else {
            _userState.value = null
        }
    }

    private fun fetchUserDetails(uid: String) {
        viewModelScope.launch {
            _loading.value = true
            val user = repository.getUserDetails(uid)
            _userState.value = user
            _loading.value = false
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).await()
                fetchUserDetails(result.user?.uid ?: "")
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }

    fun register(user: User, pass: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email, pass).await()
                val newUser = user.copy(uid = result.user?.uid ?: "")
                repository.saveUserDetails(newUser)
                _userState.value = newUser
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }
    
    fun logout() {
        repository.logout()
        _userState.value = null
    }
}
