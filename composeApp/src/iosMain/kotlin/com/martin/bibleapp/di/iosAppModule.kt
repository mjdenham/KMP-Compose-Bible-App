package com.martin.bibleapp.di

import com.martin.bibleapp.data.file.IosFileProvider
import com.martin.bibleapp.domain.file.FileProvider
import org.koin.dsl.module

val iosAppModule = module {
    single<FileProvider> { IosFileProvider() }
}