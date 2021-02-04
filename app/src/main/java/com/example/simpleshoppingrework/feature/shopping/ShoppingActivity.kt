package com.example.simpleshoppingrework.feature.shopping

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.simpleshoppingrework.R
import com.example.simpleshoppingrework.databinding.ActivityShoppingBinding
import com.example.simpleshoppingrework.db.entities.ShoppingList
import com.example.simpleshoppingrework.feature.shopping.fragments.shoppingdetails.ShoppingDetailsFragment
import com.example.simpleshoppingrework.feature.shopping.fragments.shoppinglists.ShoppingListFragment
import com.example.simpleshoppingrework.util.data.Details
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

@FlowPreview
@ExperimentalCoroutinesApi
class ShoppingActivity : AppCompatActivity() {

    lateinit var binding: ActivityShoppingBinding
    private val shoppingListFragment by inject<ShoppingListFragment>()
    private val detailsFragment by inject<ShoppingDetailsFragment>()
    val viewmodel by viewModel<ShoppingViewModel>()

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

            addShoppingProduct.setOnClickListener {
                holderAddItem.visibility = View.VISIBLE
                numberPicker.visibility = View.VISIBLE
                supportActionBar!!.apply {
                    title = ""
                    setDisplayHomeAsUpEnabled(false)
                }
                addShoppingProduct.visibility = View.GONE
            }

            cancel.setOnClickListener {
                onBackPressed()
            }

            add.setOnClickListener {
                if(inputListName.text.isNotEmpty()) {
                    when (getLastFragment()) {
                        "ShoppingListFragment" -> {
                            val currentDate = SimpleDateFormat("DD.MM", Locale.getDefault()).format(Calendar.getInstance().time)
                            viewmodel.insertList(ShoppingList(0,inputListName.text.toString(), currentDate.toString(), listOf<Details>()))
                            inputListName.text.clear()
                        }

                        "ShoppingDetailsFragment" -> {
                            lateinit var job: Job
                            job = lifecycleScope.launch {
                                viewmodel.data.collect {
                                    val details = it[viewmodel.currentList.value].details.toMutableList()
                                    details.add(Details(inputListName.text.toString(), "X: ${numberPicker.value}", false))
                                    it[viewmodel.currentList.value].details = details
                                    viewmodel.updateList(it[viewmodel.currentList.value])
                                    inputListName.text.clear()
                                    job.cancel()
                                }
                            }
                        }
                    }
                } else inputListName.error = resources.getString(R.string.text_error)
            }
        }
    }

    private fun setupToolbar() {
        binding.holderToolbar.numberPicker.apply {
            minValue = 1
            maxValue = 20
        }

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

    private fun getLastFragment(): String {
        val lastBackStackEntry = supportFragmentManager.backStackEntryCount - 1
        val fragmentName = supportFragmentManager.getBackStackEntryAt(lastBackStackEntry).name!!
        return fragmentName
    }

    fun updateDetails(details: List<Details>) {
        lateinit var job: Job
        job = lifecycleScope.launch {
            viewmodel.data.collect {
                it[viewmodel.currentList.value].details = details
                viewmodel.updateList(it[viewmodel.currentList.value])
                job.cancel()
            }
        }
    }

    fun changeFragment() {
        when (getLastFragment()) {
            "ShoppingListFragment" -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, detailsFragment)
                    addToBackStack("ShoppingDetailsFragment")
                    commit()
                }

                supportActionBar!!.apply {
                    title = "Details"
                    setDisplayHomeAsUpEnabled(true)
                }

                binding.holderToolbar.apply {
                    addShoppingList.visibility = View.GONE
                    addShoppingProduct.visibility = View.VISIBLE
					holderAddItem.visibility = View.GONE
                }
            }
            else -> onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        when (getLastFragment()) {
            "ShoppingListFragment" -> {
                if (binding.holderToolbar.holderAddItem.visibility == View.VISIBLE) {
                    binding.holderToolbar.apply {
                        holderAddItem.visibility = View.GONE
                        addShoppingList.visibility = View.VISIBLE
                    }
                    supportActionBar!!.title = "Shopping lists"
                }
                else finish()
            }
            "ShoppingDetailsFragment" -> {
                if (binding.holderToolbar.holderAddItem.visibility == View.VISIBLE) {
                    binding.holderToolbar.apply {
                        holderAddItem.visibility = View.GONE
                        addShoppingProduct.visibility = View.VISIBLE
                        numberPicker.visibility = View.GONE
                    }
                    supportActionBar!!.apply {
                        title = "Details"
                        setDisplayHomeAsUpEnabled(true)
                    }
                } else {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment_container, shoppingListFragment)
                        addToBackStack("ShoppingListFragment")
                        commit()
                    }
                    supportActionBar!!.apply {
                        title = "Shopping lists"
                        setDisplayHomeAsUpEnabled(false)
                    }
                    binding.holderToolbar.apply {
                        addShoppingProduct.visibility = View.GONE
                        addShoppingList.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    fun deleteSelectedItems(details: MutableList<Details>, selectedItems: List<Details>) {
        lateinit var job: Job
        job = lifecycleScope.launch {
            viewmodel.data.collect {
                if (it.isNotEmpty()) {
                    for (every in selectedItems) {
                        if (details.contains(every)) details.remove(every)
                    }
                    it[viewmodel.currentList.value].details = details
                    viewmodel.updateList(it[viewmodel.currentList.value])
                    viewmodel.selectedItemsCount.send(0)
                    job.cancel()
                }
            }
        }
    }
}