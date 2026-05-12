package com.example.supermarket.ui.customer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.supermarket.data.model.Order
import com.example.supermarket.databinding.ActivityCheckoutBinding
import com.example.supermarket.utils.CartManager
import com.example.supermarket.viewmodel.AuthViewModel
import com.example.supermarket.viewmodel.OrderViewModel
import com.example.supermarket.data.repository.SupermarketRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var authViewModel: AuthViewModel
    private val supermarketRepo = SupermarketRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        authViewModel.checkUser()
        authViewModel.userState.observe(this) { user ->
            user?.let {
                val addr = it.address
                binding.tvCustomerAddress.text = "${it.name}\n${it.phone}\n${addr?.street}, ${addr?.city}, ${addr?.state} - ${addr?.pincode}"
            }
        }

        MainScope().launch {
            val shop = supermarketRepo.getSupermarketInfo()
            binding.tvSupermarketUPI.text = "Pay to UPI ID: ${shop?.upiId ?: "shop@upi"}"
        }

        binding.tvTotal.text = "Total: ₹${CartManager.getTotal()}"

        binding.rgPayment.setOnCheckedChangeListener { _, checkedId ->
            binding.layoutUPI.visibility = if (checkedId == com.example.supermarket.R.id.rbUPI) View.VISIBLE else View.GONE
        }

        binding.btnPlaceOrder.setOnClickListener {
            placeOrder()
        }
    }

    private fun placeOrder() {
        val user = authViewModel.userState.value ?: return
        val paymentMethod = if (binding.rbCOD.isChecked) "COD" else "UPI"
        val txnId = binding.etTxnId.text.toString()

        if (paymentMethod == "UPI" && txnId.isEmpty()) {
            Toast.makeText(this, "Please enter Transaction ID", Toast.LENGTH_SHORT).show()
            return
        }

        val order = Order(
            customerId = user.uid,
            customerName = user.name,
            customerPhone = user.phone,
            customerAddress = binding.tvCustomerAddress.text.toString(),
            products = CartManager.getItems(),
            totalAmount = CartManager.getTotal(),
            paymentMethod = paymentMethod,
            transactionId = if (paymentMethod == "UPI") txnId else null
        )

        orderViewModel.placeOrder(order)
        CartManager.clear()
        Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_LONG).show()
        finish()
    }
}
