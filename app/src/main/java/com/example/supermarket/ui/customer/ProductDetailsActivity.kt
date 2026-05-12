package com.example.supermarket.ui.customer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.supermarket.data.model.Product
import com.example.supermarket.databinding.ActivityProductDetailsBinding
import com.example.supermarket.utils.CartManager
import com.google.firebase.firestore.FirebaseFirestore

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getStringExtra("productId") ?: return
        fetchProduct(productId)

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnAddToCart.setOnClickListener {
            product?.let {
                CartManager.addItem(it)
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchProduct(id: String) {
        FirebaseFirestore.getInstance().collection("products").document(id)
            .get().addOnSuccessListener {
                product = it.toObject(Product::class.java)
                product?.let { p ->
                    binding.tvProductName.text = p.name
                    binding.tvCategory.text = p.category
                    binding.tvPrice.text = "₹${p.price} / ${p.unit}"
                    binding.tvStock.text = "In Stock: ${p.stock} ${p.unit}"
                    binding.tvDescription.text = p.description
                    binding.btnAddToCart.isEnabled = p.stock > 0
                }
            }
    }
}
