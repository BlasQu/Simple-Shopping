package com.example.simpleshoppingrework.util

import androidx.recyclerview.widget.DiffUtil
import com.example.simpleshoppingrework.db.entities.ShoppingList

class DiffCallback(val oldList: List<ShoppingList>, val newList: List<ShoppingList>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name &&
                oldList[oldItemPosition].date == newList[newItemPosition].date
    }
}