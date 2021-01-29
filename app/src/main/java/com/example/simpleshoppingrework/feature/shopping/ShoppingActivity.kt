package com.example.simpleshoppingrework.feature.shopping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.simpleshoppingrework.R
import com.example.simpleshoppingrework.databinding.ActivityShoppingBinding
import com.example.simpleshoppingrework.feature.shopping.fragments.shoppinglists.ShoppingListFragment
import org.koin.android.ext.android.inject

class ShoppingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingBinding
    private val shoppingListFragment by inject<ShoppingListFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFragment()
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.holderToolbar.root)
        supportActionBar.apply {
            title = "Shopping lists"
        }
    }

    private fun setupFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, shoppingListFragment)
            addToBackStack("ShoppingListFragment")
            commit()
        }
    }
}