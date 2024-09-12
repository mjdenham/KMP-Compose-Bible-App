package com.martin.bibleapp.di

import com.martin.bibleapp.data.database.getDatabaseBuilder
import com.martin.bibleapp.data.file.AndroidFileProvider
import com.martin.bibleapp.data.database.BibleAppDatabase
import com.martin.bibleapp.data.database.getRoomDatabase
import com.martin.bibleapp.domain.file.FileProvider
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@OptIn(ExperimentalResourceApi::class)
val androidAppModule = module {
    single<FileProvider> { AndroidFileProvider(androidContext()) }
    single<BibleAppDatabase> { getRoomDatabase(getDatabaseBuilder(androidContext())) }
}