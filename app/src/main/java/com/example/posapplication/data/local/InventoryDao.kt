package com.example.posapplication.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface InventoryDao {

    @Query("SELECT * FROM inventory")
    fun getAllInventory(): Flow<List<InventoryEntity>>

    @Upsert
    suspend fun upsertInventory(inventoryEntity: InventoryEntity)

    @Query("DELETE FROM inventory WHERE id = :id")
    suspend fun deleteInventory(id: Int)

    @Query("SELECT COUNT(*) FROM inventory")
    fun getInventoryCount(): Flow<Int>


}



