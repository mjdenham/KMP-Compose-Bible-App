package com.martin.bibleapp.di

import com.martin.bibleapp.data.document.DocumentInstallation
import com.martin.bibleapp.data.reference.RoomCurrentReferenceRepository
import com.martin.bibleapp.data.repository.sword.SwordReader
import com.martin.bibleapp.domain.bible.Bible
import com.martin.bibleapp.domain.bible.BibleReader
import com.martin.bibleapp.domain.bible.ReferenceSelectionUseCase
import com.martin.bibleapp.domain.install.InstallBsbUseCase
import com.martin.bibleapp.domain.install.Installation
import com.martin.bibleapp.domain.reference.CurrentReferenceRepository
import com.martin.bibleapp.ui.Test.TestViewModel
import com.martin.bibleapp.ui.appsetup.AppSetupViewModel
import com.martin.bibleapp.ui.document.DocumentViewModel
import com.martin.bibleapp.ui.search.SearchViewModel
import com.martin.bibleapp.ui.selector.ChapterSelectorViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
//    single<BibleReader> { UsfmFileReader() }
    single<BibleReader> { SwordReader() }
    single<CurrentReferenceRepository> { RoomCurrentReferenceRepository(get()) }
    single<Bible> { Bible(get(), get()) }
    single<ReferenceSelectionUseCase> { ReferenceSelectionUseCase(get(), get()) }
    single<Installation> { DocumentInstallation() }
    single<InstallBsbUseCase> { InstallBsbUseCase(get()) }
    viewModel { DocumentViewModel(get()) }
    viewModel { ChapterSelectorViewModel(get(), get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { AppSetupViewModel(get()) }
    viewModel { TestViewModel(get()) }
}

fun initializeKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(appModule)
    }
}