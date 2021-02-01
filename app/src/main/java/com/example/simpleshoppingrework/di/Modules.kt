package com.example.simpleshoppingrework.di

import android.app.Application
import androidx.room.Room
import com.example.simpleshoppingrework.db.ShoppingListsDatabase
import com.example.simpleshoppingrework.feature.adapters.ShoppingDetailsAdapter
import com.example.simpleshoppingrework.feature.adapters.ShoppingListsAdapter
import com.example.simpleshoppingrework.feature.shopping.ShoppingActivity
import com.example.simpleshoppingrework.feature.shopping.ShoppingRepository
import com.example.simpleshoppingrework.feature.shopping.ShoppingViewModel
import com.example.simpleshoppingrework.feature.shopping.fragments.shoppingdetails.ShoppingDetailsFragment
import com.example.simpleshoppingrework.feature.shopping.fragments.shoppinglists.ShoppingListFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@FlowPreview
@ExperimentalCoroutinesApi
object Modules {
    val fragmentsModule = module {
        factory { ShoppingListFragment() }
        factory { ShoppingDetailsFragment() }
    }

    val databaseModule = module {

        fun provideDatabase(app: Application): ShoppingListsDatabase =
            Room.databaseBuilder(app, ShoppingListsDatabase::class.java, "ShoppingListsDatabase").build()

        fun provideDao(db: ShoppingListsDatabase) = db.getDao()

        single { provideDatabase(get()) }
        single { provideDao(get()) }
    }

    val mvvmModule = module {
        single { ShoppingRepository(get()) }
        viewModel { ShoppingViewModel(get()) }
    }

    val adaptersModule = module {
        factory { (shoppingActivity: ShoppingActivity) -> ShoppingListsAdapter(shoppingActivity) }
        factory { (shoppingActivity: ShoppingActivity) -> ShoppingDetailsAdapter(shoppingActivity) }
    }
}