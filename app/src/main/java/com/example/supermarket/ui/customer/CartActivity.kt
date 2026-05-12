package com.example.supermarket.ui.customer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.supermarket.databinding.ActivityCartBinding
import com.example.supermarket.ui.adapter.CartAdapter
import com.example.supermarket.utils.CartManager

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        updateUI()

        binding.toolbar.setNavigationOnClickListener { finish() }
        
        binding.btnCheckout.setOnClickListener {
            if (CartManager.getItems().isNotEmpty()) {
                startActivity(Intent(this, CheckoutActivity::class.java))
            } else {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = CartAdapter(CartManager.getItems()) { id, qty ->
            CartManager.updateQuantity(id, qty)
            updateUI()
        }
        binding.rvCart.layoutManager = LinearLayoutManager(this)
        binding.rvCart.adapter = adapter
    }

    private fun updateUI() {
        adapter.updateList(CartManager.getItems())
        binding.tvTotal.text = "₹${CartManager.getTotal()}"
    }
}
