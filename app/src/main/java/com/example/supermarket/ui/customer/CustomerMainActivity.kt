package com.example.supermarket.ui.customer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.supermarket.databinding.ActivityCustomerMainBinding
import com.example.supermarket.ui.adapter.CustomerProductAdapter
import com.example.supermarket.ui.auth.LoginActivity
import com.example.supermarket.utils.CartManager
import com.example.supermarket.viewmodel.AuthViewModel
import com.example.supermarket.viewmodel.ProductViewModel

class CustomerMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerMainBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var adapter: CustomerProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        setupToolbar()
        setupRecyclerView()
        setupNavigation()

        productViewModel.products.observe(this) { products ->
            adapter.updateList(products)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setupRecyclerView() {
        adapter = CustomerProductAdapter(emptyList(),
            onAddToCart = { product ->
                CartManager.addItem(product)
                Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
            },
            onClick = { product ->
                val intent = Intent(this, ProductDetailsActivity::class.java)
                intent.putExtra("productId", product.id)
                startActivity(intent)
            }
        )
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter
    }

    private fun setupNavigation() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                com.example.supermarket.R.id.nav_home -> true
                com.example.supermarket.R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                com.example.supermarket.R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

        binding.navView.setNavigationItemSelectedListener { item ->
            when(item.itemId) {
                com.example.supermarket.R.id.nav_orders -> {
                    startActivity(Intent(this, OrdersActivity::class.java))
                    true
                }
                com.example.supermarket.R.id.nav_about -> {
                    showAboutDialog()
                    true
                }
                com.example.supermarket.R.id.nav_logout -> {
                    authViewModel.logout()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                    true
                }
                else -> false
            }
        }
    private fun showAboutDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("About Fresh Mart")
            .setMessage("Fresh Mart Supermarket\n\nAddress: 123 Market Road, City Center\nPhone: 9876543210\nWorking Hours: 9:00 AM - 10:00 PM\n\nQuality products at the best prices!")
            .setPositiveButton("OK", null)
            .show()
    }
}
