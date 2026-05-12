package com.example.supermarket.ui.customer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.supermarket.data.model.Address
import com.example.supermarket.data.model.User
import com.example.supermarket.data.repository.AuthRepository
import com.example.supermarket.databinding.ActivityProfileBinding
import com.example.supermarket.viewmodel.AuthViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: AuthViewModel
    private val authRepo = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        viewModel.checkUser()
        viewModel.userState.observe(this) { user ->
            user?.let {
                binding.etName.setText(it.name)
                binding.etPhone.setText(it.phone)
                it.address?.let { addr ->
                    binding.etStreet.setText(addr.street)
                    binding.etCity.setText(addr.city)
                    binding.etState.setText(addr.state)
                    binding.etPincode.setText(addr.pincode)
                }
            }
        }

        binding.btnUpdate.setOnClickListener {
            updateProfile()
        }
    }

    private fun updateProfile() {
        val user = viewModel.userState.value ?: return
        val newName = binding.etName.text.toString()
        val newPhone = binding.etPhone.text.toString()
        val newAddress = Address(
            binding.etStreet.text.toString(),
            binding.etCity.text.toString(),
            binding.etState.text.toString(),
            binding.etPincode.text.toString()
        )

        val updatedUser = user.copy(
            name = newName,
            phone = newPhone,
            address = newAddress
        )

        MainScope().launch {
            authRepo.saveUserDetails(updatedUser)
            Toast.makeText(this@ProfileActivity, "Profile Updated", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
