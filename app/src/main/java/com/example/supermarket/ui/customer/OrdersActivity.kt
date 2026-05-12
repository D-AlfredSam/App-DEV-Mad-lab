package com.example.supermarket.ui.customer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.supermarket.databinding.ActivityOrdersBinding
import com.example.supermarket.ui.adapter.OrderAdapter
import com.example.supermarket.viewmodel.AuthViewModel
import com.example.supermarket.viewmodel.OrderViewModel

class OrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersBinding
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var adapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        setupRecyclerView()

        binding.toolbar.setNavigationOnClickListener { finish() }

        authViewModel.checkUser()
        authViewModel.userState.observe(this) { user ->
            user?.let {
                orderViewModel.getCustomerOrders(it.uid).observe(this) { orders ->
                    adapter.updateList(orders)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = OrderAdapter(emptyList(), isAdmin = false) { order ->
            // View order details
            val intent = Intent(this, OrderDetailsActivity::class.java)
            intent.putExtra("orderId", order.id)
            startActivity(intent)
        }
        binding.rvOrders.layoutManager = LinearLayoutManager(this)
        binding.rvOrders.adapter = adapter
    }
}
