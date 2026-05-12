package com.example.supermarket.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.supermarket.data.model.Address
import com.example.supermarket.data.model.User
import com.example.supermarket.databinding.ActivityRegisterBinding
import com.example.supermarket.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            val pass = binding.etPassword.text.toString()
            
            val street = binding.etStreet.text.toString()
            val city = binding.etCity.text.toString()
            val state = binding.etState.text.toString()
            val pincode = binding.etPincode.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
                val address = Address(street, city, state, pincode)
                val user = User(name = name, email = email, phone = phone, address = address)
                viewModel.register(user, pass)
            } else {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvLogin.setOnClickListener { finish() }

        viewModel.userState.observe(this) { user ->
            if (user != null) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}
