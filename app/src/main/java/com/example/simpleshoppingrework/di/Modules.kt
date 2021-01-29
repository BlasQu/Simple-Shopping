package com.example.simpleshoppingrework.di

import android.app.Application
import androidx.room.Room
import com.example.simpleshoppingrework.db.ShoppingListsDatabase
import com.example.simpleshoppingrework.feature.shopping.fragments.shoppingdetails.ShoppingDetailsFragment
import com.example.simpleshoppingrework.feature.shopping.fragments.shoppinglists.ShoppingListFragment
import org.koin.dsl.module

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
}