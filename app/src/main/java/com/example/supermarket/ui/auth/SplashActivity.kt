package com.example.supermarket.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.supermarket.ui.admin.AdminDashboardActivity
import com.example.supermarket.ui.customer.CustomerMainActivity
import com.example.supermarket.viewmodel.AuthViewModel

class SplashActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        
        // Seed initial data
        lifecycleScope.launch {
            com.example.supermarket.utils.FirestoreSeeder.seedIfNeeded()
            authViewModel.checkUser()
        }

        authViewModel.userState.observe(this) { user ->
            if (user == null) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                if (user.role == "admin") {
                    startActivity(Intent(this, AdminDashboardActivity::class.java))
                } else {
                    startActivity(Intent(this, CustomerMainActivity::class.java))
                }
            }
            finish()
        }
    }
}
