package com.example.simpleshoppingrework.feature.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleshoppingrework.db.entities.ShoppingList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ShoppingViewModel(
    val repo: ShoppingRepository
): ViewModel() {

    val data: Flow<List<ShoppingList>> = repo.readData()

    fun insertList(list: ShoppingList) {
        viewModelScope.launch {
            repo.insertList(list)
        }
    }

}