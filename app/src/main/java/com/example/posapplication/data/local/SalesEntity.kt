package com.example.posapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "sales")
data class SalesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val itemID: Int,
    val itemName: String,
    val quantity: Int,
    val price: Double,
    val date: String
)