package com.example.simpleshoppingrework.feature.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleshoppingrework.databinding.RvItemDetailsBinding
import com.example.simpleshoppingrework.db.entities.ShoppingList
import com.example.simpleshoppingrework.feature.shopping.ShoppingActivity
import com.example.simpleshoppingrework.util.DiffCallback
import com.example.simpleshoppingrework.util.DiffCallbackDetails
import com.example.simpleshoppingrework.util.data.Details
import kotlinx.coroutines.FlowPreview

@FlowPreview
class ShoppingDetailsAdapter(
        val parentContext: ShoppingActivity
): RecyclerView.Adapter<ShoppingDetailsAdapter.ViewHolder>() {

    val details = mutableListOf<Details>()

    inner class ViewHolder(val binding: RvItemDetailsBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingDetailsAdapter.ViewHolder {
        val binding = RvItemDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingDetailsAdapter.ViewHolder, position: Int) {
        holder.binding.apply {
            textProduct.text = details[position].product
            textCount.text = details[position].count
        }
    }

    override fun getItemCount(): Int {
        return 0
    }

    fun submitData(newList: List<Details>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallbackDetails(details, newList))
        details.apply {
            clear()
            addAll(newList)
        }
        diffResult.dispatchUpdatesTo(this)
    }
}