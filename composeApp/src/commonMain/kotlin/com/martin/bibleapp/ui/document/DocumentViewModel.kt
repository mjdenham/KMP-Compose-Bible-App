package com.martin.bibleapp.ui.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.bible.CurrentReferenceUseCase
import com.martin.bibleapp.domain.bible.ReadPageUseCase
import com.martin.bibleapp.domain.bible.ReferenceSelectionUseCase
import com.martin.bibleapp.domain.reference.Reference
import com.martin.bibleapp.ui.util.ResultIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.crosswire.jsword.passage.OsisParser
import org.crosswire.jsword.versification.system.Versifications

class DocumentViewModel(
    currentReferenceUseCase: CurrentReferenceUseCase,
    private val readPageUseCase: ReadPageUseCase,
    private val referenceSelectionUseCase: ReferenceSelectionUseCase): ViewModel()
{
    @OptIn(ExperimentalCoroutinesApi::class)
    val documentState = currentReferenceUseCase.getCurrentReferenceFlow()
        .filter { it.book != lastLoadedReference?.book || it.chapter != lastLoadedReference?.chapter }
        .mapLatest { reference ->
            lastLoadedReference = reference
            val page = readPageUseCase.readPage(reference)
            ResultIs.Success(DocumentModel(reference, "<html><head>${DocumentHtml.STYLE}</head><body>$page ${DocumentHtml.JAVA_SCRIPT}</body></html>"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ResultIs.Loading,
        )

    private var lastLoadedReference: Reference? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val verseState = currentReferenceUseCase.getCurrentReferenceFlow()
        .mapLatest { reference ->
            ResultIs.Success(reference)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ResultIs.Loading,
        )

    fun updateVerse(newVerseOsisId: String) {
        viewModelScope.launch {
            val verse = OsisParser().parseOsisID(DEFAULT_VERSIFICATION, newVerseOsisId)
            val reference = Reference(verse!!.book, verse.chapter, verse.verse)
            referenceSelectionUseCase.selectReference(reference)
        }
    }

    fun updateTextColour(html: String, textColour: String): String =
        DocumentHtml.updateTextColour(html, textColour)

    companion object {
        val DEFAULT_VERSIFICATION = Versifications.instance().getVersification("KJV")
    }
}