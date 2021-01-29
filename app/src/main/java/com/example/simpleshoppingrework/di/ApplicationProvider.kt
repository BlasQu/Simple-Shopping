package com.example.simpleshoppingrework.di

import android.app.Application
import com.example.simpleshoppingrework.di.Modules.databaseModule
import com.example.simpleshoppingrework.di.Modules.fragmentsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication

class ApplicationProvider: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ApplicationProvider)
            modules(listOf(fragmentsModule, databaseModule))
        }
    }
}