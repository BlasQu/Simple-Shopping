package com.example.simpleshoppingrework.feature.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleshoppingrework.R
import com.example.simpleshoppingrework.databinding.RvItemBinding
import com.example.simpleshoppingrework.db.entities.ShoppingList
import com.example.simpleshoppingrework.util.DiffCallback

class ShoppingListsAdapter: RecyclerView.Adapter<ShoppingListsAdapter.ViewHolder>() {

    val data = mutableListOf<ShoppingList>()

    inner class ViewHolder(val binding: RvItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            textName.text = data[position].name
            textDate.text = data[position].date
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun submitData(newList: List<ShoppingList>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(data, newList))
        data.apply {
            clear()
            addAll(newList)
        }
        diffResult.dispatchUpdatesTo(this)
    }
}