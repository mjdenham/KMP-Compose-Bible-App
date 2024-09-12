package com.martin.bibleapp.di

import com.martin.bibleapp.data.reference.CurrentReferenceRepository
import com.martin.bibleapp.data.repository.usfm.UsfmFileReader
import com.martin.bibleapp.domain.bible.Bible
import com.martin.bibleapp.domain.bible.BibleReader
import com.martin.bibleapp.ui.document.DocumentViewModel
import com.martin.bibleapp.ui.search.SearchViewModel
import com.martin.bibleapp.ui.selector.ChapterSelectorViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

@OptIn(ExperimentalResourceApi::class)
val appModule = module {
    single<BibleReader> { UsfmFileReader() }
    single<CurrentReferenceRepository> { CurrentReferenceRepository(get()) }
    single<Bible> { Bible(get(), get()) }
    viewModel { DocumentViewModel(get()) }
    viewModel { ChapterSelectorViewModel(get(), get()) }
    viewModel { SearchViewModel(get()) }
}

fun initializeKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(appModule)
    }
}