package com.example.posapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posapplication.data.local.InventoryEntity
import com.example.posapplication.data.local.SalesEntity
import com.example.posapplication.repo.POSRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class POSViewModel @Inject constructor(
    private val repository: POSRepository
) : ViewModel() {

    data class UiState(
        val inventory: List<InventoryEntity> = emptyList(),
        val sales: List<SalesEntity> = emptyList(),
        val inventoryCount: Int? = 0,
        val totalSales: Double? = 0.0
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchInventory()
        fetchSales()
        getInventoryCount()
        getTotalSales()
    }

    fun fetchInventory() {
        viewModelScope.launch {
            repository.getAllInventory().collect { inventory ->
                _uiState.value = _uiState.value.copy(inventory = inventory)
            }
        }
    }

    fun fetchSales() {
        viewModelScope.launch {
            repository.getAllSales().collect { sales ->
                _uiState.value = _uiState.value.copy(sales = sales)
            }
        }
    }

    fun getTotalSales() {
        viewModelScope.launch {
            repository.getTotalSales().collect { totalSales ->
                _uiState.value = _uiState.value.copy(totalSales = totalSales)
            }
        }
    }

    fun getInventoryCount() {
        viewModelScope.launch {
            repository.getInventoryCount().collect { inventoryCount ->
                _uiState.value = _uiState.value.copy(inventoryCount = inventoryCount)
            }
        }
    }

    fun insertInventory(
        name: String, unitPrice: Double, currentQuantity: Int
    ) {
        viewModelScope.launch {
            repository.upsertInventory(
                InventoryEntity(
                    name = name,
                    unitPrice = unitPrice,
                    currentQuantity = currentQuantity,
                    lastModified = System.currentTimeMillis()
                )
            )
        }
    }

    fun insertSales(
        itemID: Int,
        itemName: String,
        quantity: Int,
        price: Double,
        date: String
    ) {
        viewModelScope.launch {
            repository.insertSales(
                SalesEntity(
                    itemID = itemID,
                    itemName = itemName,
                    quantity = quantity,
                    price = price,
                    date = date
                )
            )
        }
    }

    fun getTotalPrice(price: Double, quantity: Int): Double {
        return repository.getTotalPrice(price, quantity)
    }

    fun deleteInventory(id: Int) {
        viewModelScope.launch {
            repository.deleteInventory(id)
        }
    }
}
