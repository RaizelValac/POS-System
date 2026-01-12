package com.example.posapplication.repo

import com.example.posapplication.data.local.InventoryDao
import com.example.posapplication.data.local.InventoryEntity
import com.example.posapplication.data.local.SalesDao
import com.example.posapplication.data.local.SalesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class POSRepository@Inject constructor(
    private val inventoryDao: InventoryDao,
    private val salesDao: SalesDao
) {
    fun getAllInventory(): Flow<List<InventoryEntity>> {
        return inventoryDao.getAllInventory()
    }

    fun getAllSales(): Flow<List<SalesEntity>> {
        return salesDao.getAllSales()
    }

    fun getInventoryCount(): Flow<Int> {
        return inventoryDao.getInventoryCount()
    }

    fun getTotalSales(): Flow<Double> {
        return salesDao.getTotalSales()
    }

    fun getTotalPrice(price: Double, quantity: Int): Double {
        return price * quantity
    }

    suspend fun upsertInventory(inventoryEntity: InventoryEntity) {
        inventoryDao.upsertInventory(inventoryEntity)
    }

    suspend fun insertSales(salesEntity: SalesEntity) {
        salesDao.sellItem(salesEntity)
    }

    suspend fun deleteInventory(id: Int) {
        inventoryDao.deleteInventory(id)
    }

}