package com.example.supermarket.ui.customer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.supermarket.data.model.Order
import com.example.supermarket.databinding.ActivityOrderDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore

class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderId = intent.getStringExtra("orderId") ?: return
        fetchOrder(orderId)

        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun fetchOrder(id: String) {
        FirebaseFirestore.getInstance().collection("orders").document(id)
            .get().addOnSuccessListener {
                val order = it.toObject(Order::class.java)
                order?.let { o ->
                    binding.tvOrderId.text = "Order ID: ${o.id}"
                    binding.tvStatus.text = "Status: ${o.status}"
                    binding.tvTotal.text = "Total: ₹${o.totalAmount}"
                    binding.tvPayment.text = "Payment: ${o.paymentMethod} ${o.transactionId ?: ""}"
                    binding.tvAddress.text = o.customerAddress
                    
                    val productsStr = o.products.joinToString("\n") { p -> 
                        "${p.name} x ${p.quantity} = ₹${p.price * p.quantity}"
                    }
                    binding.tvProducts.text = productsStr
                }
            }
    }
}
