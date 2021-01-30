package com.example.simpleshoppingrework.feature.shopping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.simpleshoppingrework.R
import com.example.simpleshoppingrework.databinding.ActivityShoppingBinding
import com.example.simpleshoppingrework.db.entities.ShoppingList
import com.example.simpleshoppingrework.feature.shopping.fragments.shoppinglists.ShoppingListFragment
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ShoppingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingBinding
    private val shoppingListFragment by inject<ShoppingListFragment>()
    private val viewmodel by viewModel<ShoppingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFragment()
        setupToolbar()
        setupButtons()
    }

    private fun setupButtons() {
        binding.holderToolbar.apply {
            addShoppingList.setOnClickListener {
                holderAddItem.visibility = View.VISIBLE
                supportActionBar!!.title = ""
                addShoppingList.visibility = View.GONE
            }
            cancel.setOnClickListener {
                holderAddItem.visibility = View.GONE
                supportActionBar!!.title = "Shopping lists"
                addShoppingList.visibility = View.VISIBLE
            }
            add.setOnClickListener {
                if(inputListName.text.isNotEmpty()) {
                    val currentDate = SimpleDateFormat("DD.MM", Locale.getDefault()).format(Calendar.getInstance().time)
                    viewmodel.insertList(ShoppingList(0,inputListName.text.toString(), currentDate.toString()))
                    inputListName.text.clear()
                } else inputListName.error = resources.getString(R.string.text_error)
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.holderToolbar.toolbar)
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