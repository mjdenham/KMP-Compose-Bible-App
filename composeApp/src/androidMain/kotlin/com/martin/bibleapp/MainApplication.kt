package com.martin.bibleapp

import android.app.Application
import com.martin.bibleapp.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initializeKoin() {
            androidContext(this@MainApplication)
        }
    }
}