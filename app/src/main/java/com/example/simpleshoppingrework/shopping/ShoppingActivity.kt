package com.example.simpleshoppingrework.shopping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.simpleshoppingrework.R
import com.example.simpleshoppingrework.databinding.ActivityShoppingBinding
import com.example.simpleshoppingrework.shopping.fragments.ShoppingListFragment
import org.koin.android.ext.android.inject

class ShoppingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingBinding
    private val shoppingListFragment by inject<ShoppingListFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFragment()
    }

    private fun setupFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, shoppingListFragment)
            addToBackStack("ShoppingListFragment")
            commit()
        }
    }
}