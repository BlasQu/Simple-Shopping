package com.example.simpleshoppingrework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simpleshoppingrework.db.converters.ConverterToJson
import com.example.simpleshoppingrework.db.entities.ShoppingList

@TypeConverters(ConverterToJson::class)
@Database(version = 1, entities = [ShoppingList::class])
abstract class ShoppingListsDatabase: RoomDatabase() {
    abstract fun getDao(): ShoppingListsDao
}