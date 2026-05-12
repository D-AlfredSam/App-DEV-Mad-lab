package com.example.supermarket.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.supermarket.data.model.Product
import com.example.supermarket.databinding.ItemProductCustomerBinding

class CustomerProductAdapter(
    private var products: List<Product>,
    private val onAddToCart: (Product) -> Unit,
    private val onClick: (Product) -> Unit
) : RecyclerView.Adapter<CustomerProductAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemProductCustomerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.binding.tvName.text = product.name
        holder.binding.tvPrice.text = "₹${product.price} / ${product.unit}"
        
        holder.binding.tvStock.text = "Stock: ${product.stock} ${product.unit}"
        if (product.stock in 1..5) {
            holder.binding.tvStock.setTextColor(Color.RED)
            holder.binding.tvStock.text = "Low Stock: ${product.stock} ${product.unit}!"
        } else {
            holder.binding.tvStock.setTextColor(Color.GRAY)
        }

        holder.binding.btnAddToCart.isEnabled = product.stock > 0
        holder.binding.btnAddToCart.text = if (product.stock > 0) "Add" else "Out"

        holder.binding.btnAddToCart.setOnClickListener { onAddToCart(product) }
        holder.binding.root.setOnClickListener { onClick(product) }
    }

    override fun getItemCount() = products.size

    fun updateList(newList: List<Product>) {
        products = newList
        notifyDataSetChanged()
    }
}
