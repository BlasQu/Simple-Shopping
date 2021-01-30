package com.example.simpleshoppingrework.feature.shopping

import com.example.simpleshoppingrework.db.ShoppingListsDao
import com.example.simpleshoppingrework.db.entities.ShoppingList
import kotlinx.coroutines.flow.Flow

class ShoppingRepository(
    val dao: ShoppingListsDao
) {

    suspend fun insertList(list: ShoppingList) = dao.insertList(list)
    fun readData(): Flow<List<ShoppingList>> = dao.readData()

}