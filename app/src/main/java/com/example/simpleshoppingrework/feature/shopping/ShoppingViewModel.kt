package com.example.simpleshoppingrework.feature.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleshoppingrework.db.entities.ShoppingList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ShoppingViewModel(
    val repo: ShoppingRepository
): ViewModel() {

    val data: Flow<List<ShoppingList>> = repo.readData()
    val currentList = ConflatedBroadcastChannel<Int>()
    val selectedItemsCount = ConflatedBroadcastChannel<Int>()

    fun insertList(list: ShoppingList) {
        viewModelScope.launch {
            repo.insertList(list)
        }
    }

    fun deleteList(lists: List<ShoppingList>) {
        viewModelScope.launch {
            repo.deleteLists(lists)
        }
    }

    fun updateList(list: ShoppingList) {
        viewModelScope.launch {
            repo.updateList(list)
        }
    }
}