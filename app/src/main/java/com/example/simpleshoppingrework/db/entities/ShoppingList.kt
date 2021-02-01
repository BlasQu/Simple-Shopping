package com.example.simpleshoppingrework.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simpleshoppingrework.util.data.Details
import java.util.*

@Entity(tableName = "ShoppingLists")
data class ShoppingList(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "details") var details: List<Details>
    )