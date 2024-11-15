package com.martin.bibleapp.ui.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.bible.CurrentReferenceUseCase
import com.martin.bibleapp.domain.bible.ReadPageUseCase
import com.martin.bibleapp.domain.bible.ReferenceSelectionUseCase
import com.martin.bibleapp.ui.util.ResultIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.crosswire.jsword.passage.OsisParser
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.versification.system.Versifications

class DocumentViewModel(
    currentReferenceUseCase: CurrentReferenceUseCase,
    private val readPageUseCase: ReadPageUseCase,
    private val referenceSelectionUseCase: ReferenceSelectionUseCase): ViewModel()
{
    @OptIn(ExperimentalCoroutinesApi::class)
    val documentState = currentReferenceUseCase.getCurrentReferenceFlow()
        .filter { it.book != lastLoadedVerse?.book || it.chapter != lastLoadedVerse?.chapter }
        .mapLatest { verse ->
            lastLoadedVerse = verse
            val page = readPageUseCase.readPage(verse)
            ResultIs.Success(DocumentModel(verse, "<html><head>${DocumentHtml.STYLE}</head><body>$page ${DocumentHtml.JAVA_SCRIPT}</body></html>"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ResultIs.Loading,
        )

    private var lastLoadedVerse: Verse? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val verseState = currentReferenceUseCase.getCurrentReferenceFlow()
        .mapLatest { verse ->
            ResultIs.Success(verse)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ResultIs.Loading,
        )

    fun updateVerse(newVerseOsisId: String) {
        viewModelScope.launch {
            OsisParser().parseOsisID(DEFAULT_VERSIFICATION, newVerseOsisId)?.let { verse ->
                val reference = Verse(DEFAULT_VERSIFICATION, verse.book, verse.chapter, verse.verse)
                referenceSelectionUseCase.selectReference(reference)
            }
        }
    }

    fun updateTextColour(html: String, textColour: String): String =
        DocumentHtml.updateTextColour(html, textColour)

    companion object {
        val DEFAULT_VERSIFICATION = Versifications.instance().getVersification("KJV")
    }
}