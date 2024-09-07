package com.martin.bibleapp.di

import com.martin.bibleapp.data.file.AndroidFileProvider
import com.martin.bibleapp.domain.file.FileProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidAppModule = module {
    single<FileProvider> { AndroidFileProvider(androidContext()) }
}