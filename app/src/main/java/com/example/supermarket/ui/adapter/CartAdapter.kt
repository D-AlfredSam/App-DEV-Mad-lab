package com.example.supermarket.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.supermarket.data.model.CartItem
import com.example.supermarket.databinding.ItemCartBinding

class CartAdapter(
    private var items: List<CartItem>,
    private val onUpdate: (String, Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvName.text = item.name
        holder.binding.tvPrice.text = "₹${item.price} x ${item.quantity} ${item.unit} = ₹${item.price * item.quantity}"
        holder.binding.tvQuantity.text = item.quantity.toString()

        holder.binding.btnAdd.setOnClickListener { onUpdate(item.productId, item.quantity + 1) }
        holder.binding.btnRemove.setOnClickListener { onUpdate(item.productId, item.quantity - 1) }
        holder.binding.btnDelete.setOnClickListener { onUpdate(item.productId, 0) }
    }

    override fun getItemCount() = items.size

    fun updateList(newList: List<CartItem>) {
        items = newList
        notifyDataSetChanged()
    }
}
