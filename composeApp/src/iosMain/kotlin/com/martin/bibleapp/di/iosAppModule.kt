package com.martin.bibleapp.di

import com.martin.bibleapp.data.database.getDatabaseBuilder
import com.martin.bibleapp.data.file.IosFileProvider
import com.martin.bibleapp.data.database.BibleAppDatabase
import com.martin.bibleapp.data.database.getRoomDatabase
import com.martin.bibleapp.domain.file.FileProvider
import org.koin.dsl.module

val iosAppModule = module {
    single<FileProvider> { IosFileProvider() }
    single<BibleAppDatabase> { getRoomDatabase(getDatabaseBuilder()) }
}