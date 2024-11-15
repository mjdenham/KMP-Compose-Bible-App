package com.martin.bibleapp.di

import com.martin.bibleapp.data.document.DocumentInstallation
import com.martin.bibleapp.data.reference.RoomCurrentVerseRepository
import com.martin.bibleapp.data.repository.sword.SwordReader
import com.martin.bibleapp.domain.bible.BibleReader
import com.martin.bibleapp.domain.bible.CurrentReferenceUseCase
import com.martin.bibleapp.domain.bible.ReadPageUseCase
import com.martin.bibleapp.domain.bible.ReferenceSelectionUseCase
import com.martin.bibleapp.domain.bible.SearchUseCase
import com.martin.bibleapp.domain.install.InstallBsbUseCase
import com.martin.bibleapp.domain.install.Installation
import com.martin.bibleapp.domain.reference.CurrentReferenceRepository
import com.martin.bibleapp.ui.appsetup.AppSetupViewModel
import com.martin.bibleapp.ui.document.DocumentViewModel
import com.martin.bibleapp.ui.search.SearchViewModel
import com.martin.bibleapp.ui.selector.BookSelectorViewModel
import com.martin.bibleapp.ui.selector.ChapterSelectorViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    singleOf(::SwordReader) { bind<BibleReader>() }
    singleOf(::RoomCurrentVerseRepository) { bind<CurrentReferenceRepository>() }
    singleOf(::DocumentInstallation) { bind<Installation>() }

    singleOf(::CurrentReferenceUseCase)
    singleOf(::ReferenceSelectionUseCase)
    singleOf(::InstallBsbUseCase)
    singleOf(::ReadPageUseCase)
    singleOf(::SearchUseCase)
    singleOf(::CurrentReferenceUseCase)

    viewModelOf(::DocumentViewModel)
    viewModelOf(::BookSelectorViewModel)
    viewModelOf(::ChapterSelectorViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::AppSetupViewModel)
}

private var koinStarted = false

fun initializeKoin(appDeclaration: KoinAppDeclaration = {}) {
    // Compose 1.7.0 seems to have a bug which causes MainViewController to be created twice, consequently initializeKoin would be called twice
    if (!koinStarted) {
        koinStarted = true

        startKoin {
            appDeclaration()
            modules(appModule)
        }
    }
}