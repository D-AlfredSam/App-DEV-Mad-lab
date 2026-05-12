package com.example.supermarket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.supermarket.data.model.Order
import com.example.supermarket.data.repository.OrderRepository
import com.example.supermarket.data.repository.ProductRepository
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepository: OrderRepository = OrderRepository(),
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {

    val allOrders = orderRepository.getAllOrders().asLiveData()

    fun getCustomerOrders(customerId: String) = 
        orderRepository.getCustomerOrders(customerId).asLiveData()

    fun placeOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.placeOrder(order)
            // Reduce stock
            order.products.forEach { item ->
                productRepository.updateStock(item.productId, -item.quantity)
            }
        }
    }

    fun updateStatus(orderId: String, status: String) {
        viewModelScope.launch {
            orderRepository.updateOrderStatus(orderId, status)
        }
    }
}
