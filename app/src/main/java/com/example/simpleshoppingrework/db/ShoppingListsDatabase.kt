package com.example.simpleshoppingrework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.simpleshoppingrework.db.entities.ShoppingList

@Database(version = 1, entities = [ShoppingList::class])
abstract class ShoppingListsDatabase: RoomDatabase() {
    abstract fun getDao(): ShoppingListsDao
}