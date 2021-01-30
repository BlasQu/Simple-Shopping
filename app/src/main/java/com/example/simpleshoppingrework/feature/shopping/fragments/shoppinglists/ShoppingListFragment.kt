package com.example.simpleshoppingrework.feature.shopping.fragments.shoppinglists

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpleshoppingrework.R
import com.example.simpleshoppingrework.databinding.FragmentShoppingListBinding
import com.example.simpleshoppingrework.feature.adapters.ShoppingListsAdapter
import com.example.simpleshoppingrework.feature.shopping.ShoppingActivity
import com.example.simpleshoppingrework.feature.shopping.ShoppingViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ShoppingListFragment : Fragment(R.layout.fragment_shopping_list) {

    private lateinit var binding: FragmentShoppingListBinding
    private val viewmodel by sharedViewModel<ShoppingViewModel>()
    private val shoppingListsAdapter by inject<ShoppingListsAdapter>()
    private lateinit var shoppingActivity: ShoppingActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentShoppingListBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        shoppingActivity = activity as ShoppingActivity

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val decorationLine = DividerItemDecoration(shoppingActivity, DividerItemDecoration.VERTICAL).apply {
            setDrawable(resources.getDrawable(R.drawable.decoration_line))
        }
        binding.rvShoppingLists.apply {
            layoutManager = LinearLayoutManager(shoppingActivity)
            this.adapter = shoppingListsAdapter
            addItemDecoration(decorationLine)
        }

        lifecycleScope.launch {
            viewmodel.data.collect {
                shoppingListsAdapter.submitData(it)
            }
        }
    }

}