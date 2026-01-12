package com.example.posapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "inventory")
data class InventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val unitPrice: Double,
    val currentQuantity: Int,
    val lastModified: Long = System.currentTimeMillis()
)
