package com.example.supermarket.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.supermarket.data.model.Order
import com.example.supermarket.databinding.ActivityManageOrdersBinding
import com.example.supermarket.ui.adapter.OrderAdapter
import com.example.supermarket.viewmodel.OrderViewModel

class ManageOrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageOrdersBinding
    private lateinit var viewModel: OrderViewModel
    private lateinit var adapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        setupRecyclerView()

        viewModel.allOrders.observe(this) { orders ->
            adapter.updateList(orders)
        }
    }

    private fun setupRecyclerView() {
        adapter = OrderAdapter(emptyList(), isAdmin = true) { order ->
            showStatusDialog(order)
        }
        binding.rvOrders.layoutManager = LinearLayoutManager(this)
        binding.rvOrders.adapter = adapter
    }

    private fun showStatusDialog(order: Order) {
        val statuses = arrayOf("Pending", "Accepted", "Packed", "Delivered", "Cancelled")
        AlertDialog.Builder(this)
            .setTitle("Update Status")
            .setItems(statuses) { _, which ->
                viewModel.updateStatus(order.id, statuses[which])
            }
            .show()
    }
}
