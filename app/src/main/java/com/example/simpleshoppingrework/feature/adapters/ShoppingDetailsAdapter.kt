package com.example.simpleshoppingrework.feature.adapters

import android.graphics.Paint
import android.util.Log
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleshoppingrework.R
import com.example.simpleshoppingrework.databinding.RvItemBinding
import com.example.simpleshoppingrework.databinding.RvItemDetailsBinding
import com.example.simpleshoppingrework.db.entities.ShoppingList
import com.example.simpleshoppingrework.feature.shopping.ShoppingActivity
import com.example.simpleshoppingrework.util.DiffCallback
import com.example.simpleshoppingrework.util.DiffCallbackDetails
import com.example.simpleshoppingrework.util.data.Details
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
class ShoppingDetailsAdapter(
        private val parentContext: ShoppingActivity
): RecyclerView.Adapter<ShoppingDetailsAdapter.ViewHolder>() {

    private val details = mutableListOf<Details>()
    private val selectedItems = mutableListOf<Details>()
    private val viewmodel = parentContext.viewmodel

    private var isEnabled = false
    private var allSelected = false

    private val color_cyan = parentContext.resources.getColor(R.color.selected_item_cyan)
    private val color_light_green = parentContext.resources.getColor(R.color.light_green)
    private val color_white = parentContext.resources.getColor(R.color.white)

    inner class ViewHolder(val binding: RvItemDetailsBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (!isEnabled) {
                    checkedConfig(binding, adapterPosition)
                } else {
                    itemSelect(binding, adapterPosition)
                }
            }

            binding.root.setOnLongClickListener {
                lateinit var job: Job
                if (!isEnabled) parentContext.binding.holderToolbar.toolbar.startActionMode(
                        object : ActionMode.Callback {
                            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                                mode!!.menuInflater.inflate(R.menu.menu_action_mode, menu)
                                return true
                            }

                            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                                isEnabled = true
                                itemSelect(binding, adapterPosition)

                                job = parentContext.lifecycleScope.launch {
                                    viewmodel.selectedItemsCount.asFlow().collect {
                                        mode!!.title = "Selected items: $it"
                                    }
                                }
                                return true
                            }

                            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                                when (item!!.itemId) {
                                    R.id.select_all -> {
                                        if (selectedItems.size == details.size) {
                                            allSelected = false
                                            selectedItems.clear()
                                            viewmodel.selectedItemsCount.offer(selectedItems.size)
                                        } else {
                                            allSelected = true
                                            selectedItems.clear()
                                            selectedItems.addAll(details)
                                            viewmodel.selectedItemsCount.offer(selectedItems.size)
                                        }
                                        notifyDataSetChanged()
                                    }
                                    R.id.delete -> {
                                        if (selectedItems.isEmpty()) Snackbar.make(parentContext.binding.root, "You have not selected any item!", Snackbar.LENGTH_SHORT).show()
                                        else {
                                            parentContext.deleteSelectedItems(details, selectedItems)
                                            notifyDataSetChanged()
                                        }
                                    }
                                }
                                return true
                            }

                            override fun onDestroyActionMode(mode: ActionMode?) {
                                isEnabled = false
                                allSelected = false
                                selectedItems.clear()
                                job.cancel()
                                notifyDataSetChanged()
                            }
                        }
                )
                return@setOnLongClickListener true
            }
        }
    }

    private fun retrieveDataAfterUnselecting(binding: RvItemDetailsBinding, adapterPosition: Int) {
        if (details[adapterPosition].checked) {
            binding.root.setBackgroundColor(color_light_green)
            binding.textProduct.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.textCount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.iconMark.setImageDrawable(parentContext.resources.getDrawable(R.drawable.ic_baseline_check_circle_24))
        } else {
            binding.root.setBackgroundColor(color_white)
            binding.textProduct.paintFlags = 0
            binding.textCount.paintFlags = 0
            binding.iconMark.setImageDrawable(parentContext.resources.getDrawable(R.drawable.ic_baseline_check_circle_outline_24))
        }
    }

    private fun checkedConfig(binding: RvItemDetailsBinding, adapterPosition: Int) {
        if (!details[adapterPosition].checked) {
            binding.root.setBackgroundColor(color_light_green)
            binding.textProduct.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.textCount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.iconMark.setImageDrawable(parentContext.resources.getDrawable(R.drawable.ic_baseline_check_circle_24))
            details[adapterPosition].checked = true
            parentContext.updateDetails(details)
        } else {
            binding.root.setBackgroundColor(color_white)
            binding.textProduct.paintFlags = 0
            binding.textCount.paintFlags = 0
            binding.iconMark.setImageDrawable(parentContext.resources.getDrawable(R.drawable.ic_baseline_check_circle_outline_24))
            details[adapterPosition].checked = false
            parentContext.updateDetails(details)
        }
    }

    private fun itemSelect(binding: RvItemDetailsBinding, adapterPosition: Int) {
        if (selectedItems.contains(details[adapterPosition])) {
            selectedItems.remove(details[adapterPosition])
            retrieveDataAfterUnselecting(binding, adapterPosition)
        } else {
            selectedItems.add(details[adapterPosition])
            binding.root.setBackgroundColor(color_cyan)
        }
        viewmodel.selectedItemsCount.offer(selectedItems.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingDetailsAdapter.ViewHolder {
        val binding = RvItemDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingDetailsAdapter.ViewHolder, position: Int) {
        holder.binding.apply {
            textProduct.text = details[position].product
            textCount.text = details[position].count

            if (details[position].checked) {
                root.setBackgroundColor(color_light_green)
                textProduct.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                textCount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                iconMark.setImageDrawable(parentContext.resources.getDrawable(R.drawable.ic_baseline_check_circle_24))
            } else {
                root.setBackgroundColor(color_white)
                textProduct.paintFlags = 0
                textCount.paintFlags = 0
                iconMark.setImageDrawable(parentContext.resources.getDrawable(R.drawable.ic_baseline_check_circle_outline_24))
            }

            if (allSelected) holder.binding.root.setBackgroundColor(color_cyan)
            else if (!details[position].checked) holder.binding.root.setBackgroundColor(color_white)
            else holder.binding.root.setBackgroundColor(color_light_green)
        }
    }

    override fun getItemCount(): Int {
        return details.size
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