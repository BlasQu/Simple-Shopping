package com.example.simpleshoppingrework.di

import com.example.simpleshoppingrework.shopping.fragments.ShoppingListFragment
import org.koin.dsl.module

object Modules {
    val fragmentsModule = module {
        factory { ShoppingListFragment() }
    }
}