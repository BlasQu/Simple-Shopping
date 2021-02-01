package com.example.simpleshoppingrework.feature.adapters

import android.util.Log
import android.view.*
import android.widget.ActionMenuView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleshoppingrework.R
import com.example.simpleshoppingrework.databinding.RvItemBinding
import com.example.simpleshoppingrework.db.entities.ShoppingList
import com.example.simpleshoppingrework.feature.shopping.ShoppingActivity
import com.example.simpleshoppingrework.feature.shopping.ShoppingViewModel
import com.example.simpleshoppingrework.feature.shopping.fragments.shoppinglists.ShoppingListFragment
import com.example.simpleshoppingrework.util.DiffCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.bind
import kotlin.coroutines.coroutineContext

@ExperimentalCoroutinesApi
class ShoppingListsAdapter(
        val parentContext: ShoppingActivity,
): RecyclerView.Adapter<ShoppingListsAdapter.ViewHolder>() {

    private val data = mutableListOf<ShoppingList>()
    private val selectedItems = mutableListOf<ShoppingList>()
    private val viewmodel = parentContext.viewmodel

    private var isEnabled = false
    private var allSelected = false

    private val cyanColor = parentContext.resources.getColor(R.color.selected_item_cyan)
    private val whiteColor = parentContext.resources.getColor(R.color.white)

    @FlowPreview
    inner class ViewHolder(val binding: RvItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (isEnabled) {
                    itemSelect(binding, adapterPosition)
                } else {
                    parentContext.changeFragment()
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
                                        if (selectedItems.size == data.size) {
                                            allSelected = false
                                            selectedItems.clear()
                                            viewmodel.selectedItemsCount.offer(selectedItems.size)
                                        }
                                        else {
                                            allSelected = true
                                            selectedItems.clear()
                                            selectedItems.addAll(data)
                                            viewmodel.selectedItemsCount.offer(selectedItems.size)
                                        }
                                        notifyDataSetChanged()
                                    }
                                    R.id.delete -> {
                                        if (selectedItems.isEmpty()) Snackbar.make(parentContext.binding.root, "You have not selected any item!", Snackbar.LENGTH_SHORT).show()
                                        else deleteSelectedItems()
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

    private fun deleteSelectedItems() {
        viewmodel.deleteList(selectedItems)
        viewmodel.selectedItemsCount.offer(0)
    }

    private fun itemSelect(binding: RvItemBinding, adapterPosition: Int) {
        if (selectedItems.contains(data[adapterPosition])) {
            selectedItems.remove(data[adapterPosition])
            binding.root.setBackgroundColor(whiteColor)
        } else {
            selectedItems.add(data[adapterPosition])
            binding.root.setBackgroundColor(cyanColor)
        }
        viewmodel.selectedItemsCount.offer(selectedItems.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            textName.text = data[position].name
            textDate.text = data[position].date
        }

        if (allSelected) holder.binding.root.setBackgroundColor(cyanColor)
        else holder.binding.root.setBackgroundColor(whiteColor)
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