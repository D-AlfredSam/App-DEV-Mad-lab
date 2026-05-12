package com.example.supermarket.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.supermarket.databinding.ActivityAdminDashboardBinding
import com.example.supermarket.ui.auth.LoginActivity
import com.example.supermarket.viewmodel.AuthViewModel

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.cardManageProducts.setOnClickListener {
            startActivity(Intent(this, ManageProductsActivity::class.java))
        }

        binding.cardManageOrders.setOnClickListener {
            startActivity(Intent(this, ManageOrdersActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            authViewModel.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }
}
