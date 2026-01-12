package com.example.posapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [InventoryEntity::class, SalesEntity::class], version = 2, exportSchema = false)
abstract class POSDatabase: RoomDatabase() {
    abstract fun getInventoryDao(): InventoryDao
    abstract fun getSalesDao(): SalesDao
}