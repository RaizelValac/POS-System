package com.example.posapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SalesDao {

    @Query("SELECT * FROM sales")
    fun getAllSales(): Flow<List<SalesEntity>>

    @Query("SELECT COALESCE(SUM(price), 0.0) FROM sales")
    fun getTotalSales(): Flow<Double>

    @Insert
    suspend fun insertSales(salesEntity: SalesEntity)

    @Query("UPDATE inventory SET currentQuantity = currentQuantity - :quantity WHERE id = :id")
    suspend fun decreaseStock(id: Int, quantity: Int)

    @Transaction
    suspend fun sellItem(salesEntity: SalesEntity) {
        insertSales(salesEntity)
        decreaseStock(salesEntity.itemID, salesEntity.quantity)
    }
}
