package com.example.simpleshoppingrework.feature.shopping

import com.example.simpleshoppingrework.db.ShoppingListsDao
import com.example.simpleshoppingrework.db.entities.ShoppingList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ShoppingRepository(
    val dao: ShoppingListsDao
) {

    suspend fun insertList(list: ShoppingList) = dao.insertList(list)
    suspend fun deleteLists(lists: List<ShoppingList>) = dao.deleteLists(lists)
    suspend fun updateList(list: ShoppingList) = dao.updateList(list)
    fun readData(): Flow<List<ShoppingList>> = dao.readData()

}