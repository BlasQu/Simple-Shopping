package com.example.simpleshoppingrework.feature.shopping.fragments.shoppingdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpleshoppingrework.R
import com.example.simpleshoppingrework.databinding.FragmentShoppingDetailsBinding
import com.example.simpleshoppingrework.feature.adapters.ShoppingDetailsAdapter
import com.example.simpleshoppingrework.feature.shopping.ShoppingActivity
import com.example.simpleshoppingrework.feature.shopping.ShoppingViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

@FlowPreview
@ExperimentalCoroutinesApi
class ShoppingDetailsFragment: Fragment(R.layout.fragment_shopping_details) {

    private lateinit var binding: FragmentShoppingDetailsBinding
    private lateinit var shoppingActivity: ShoppingActivity

    private val viewmodel by sharedViewModel<ShoppingViewModel>()
    private val detailsAdapter by inject<ShoppingDetailsAdapter>() {
        parametersOf(shoppingActivity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentShoppingDetailsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        shoppingActivity = activity as ShoppingActivity

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val decorationLine = DividerItemDecoration(shoppingActivity, DividerItemDecoration.VERTICAL).apply {
            setDrawable(resources.getDrawable(R.drawable.decoration_line))
        }
        binding.rvDetails.apply {
            layoutManager = LinearLayoutManager(shoppingActivity)
            this.adapter = detailsAdapter
            addItemDecoration(decorationLine)
        }
    }
}