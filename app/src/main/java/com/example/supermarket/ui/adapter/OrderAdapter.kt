package com.example.supermarket.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.supermarket.data.model.Order
import com.example.supermarket.databinding.ItemOrderBinding

class OrderAdapter(
    private var orders: List<Order>,
    private val isAdmin: Boolean,
    private val onClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        holder.binding.tvOrderId.text = "Order ID: ${order.id}"
        holder.binding.tvDate.text = order.timestamp.toDate().toString()
        holder.binding.tvTotal.text = "Total: ₹${order.totalAmount}"
        holder.binding.tvStatus.text = order.status
        
        if (isAdmin) {
            holder.binding.tvCustomer.text = "Customer: ${order.customerName}"
        } else {
            holder.binding.tvCustomer.visibility = android.view.View.GONE
        }

        holder.binding.root.setOnClickListener { onClick(order) }
    }

    override fun getItemCount() = orders.size

    fun updateList(newList: List<Order>) {
        orders = newList
        notifyDataSetChanged()
    }
}
