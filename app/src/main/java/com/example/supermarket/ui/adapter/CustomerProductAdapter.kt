package com.example.supermarket.ui.adapter

import android.view.LayoutInflater
import android.view.View
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
        val binding = ItemProductCustomerBinding.inflate(LayoutInflater.from(parent.context), parent.context, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.binding.tvProductName.text = product.name
        holder.binding.tvCategory.text = product.category
        holder.binding.tvPrice.text = "₹${product.price} / ${product.unit}"
        
        holder.binding.tvLowStock.visibility = if (product.stock in 1..5) View.VISIBLE else View.GONE
        holder.binding.btnAddToCart.isEnabled = product.stock > 0
        holder.binding.btnAddToCart.text = if (product.stock > 0) "Add to Cart" else "Out of Stock"

        holder.binding.btnAddToCart.setOnClickListener { onAddToCart(product) }
        holder.binding.root.setOnClickListener { onClick(product) }
    }

    override fun getItemCount() = products.size

    fun updateList(newList: List<Product>) {
        products = newList
        notifyDataSetChanged()
    }
}
