package com.example.supermarket.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.supermarket.data.model.Product
import com.example.supermarket.databinding.ActivityManageProductsBinding
import com.example.supermarket.databinding.DialogAddProductBinding
import com.example.supermarket.ui.adapter.ProductAdapter
import com.example.supermarket.viewmodel.ProductViewModel

class ManageProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageProductsBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        
        setupRecyclerView()

        binding.fabAddProduct.setOnClickListener {
            showAddEditDialog(null)
        }

        viewModel.products.observe(this) { products ->
            adapter.updateList(products)
        }
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(emptyList(), 
            onEdit = { showAddEditDialog(it) },
            onDelete = { showDeleteConfirmation(it) }
        )
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter
    }

    private fun showAddEditDialog(product: Product?) {
        val dialogBinding = DialogAddProductBinding.inflate(LayoutInflater.from(this))
        val builder = AlertDialog.Builder(this).setView(dialogBinding.root)
        
        if (product != null) {
            dialogBinding.tvTitle.text = "Edit Product"
            dialogBinding.etProductName.setText(product.name)
            dialogBinding.etCategory.setText(product.category)
            dialogBinding.etPrice.setText(product.price.toString())
            dialogBinding.etUnit.setText(product.unit)
            dialogBinding.etStock.setText(product.stock.toString())
            dialogBinding.etDescription.setText(product.description)
        }

        builder.setPositiveButton(if (product == null) "Add" else "Update") { _, _ ->
            val name = dialogBinding.etProductName.text.toString()
            val category = dialogBinding.etCategory.text.toString()
            val price = dialogBinding.etPrice.text.toString().toDoubleOrNull() ?: 0.0
            val unit = dialogBinding.etUnit.text.toString()
            val stock = dialogBinding.etStock.text.toString().toIntOrNull() ?: 0
            val desc = dialogBinding.etDescription.text.toString()

            if (name.isNotEmpty()) {
                val newProduct = Product(
                    id = product?.id ?: "",
                    name = name,
                    category = category,
                    price = price,
                    unit = unit,
                    stock = stock,
                    description = desc
                )
                if (product == null) viewModel.addProduct(newProduct)
                else viewModel.updateProduct(newProduct)
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun showDeleteConfirmation(product: Product) {
        AlertDialog.Builder(this)
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete ${product.name}?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteProduct(product.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
