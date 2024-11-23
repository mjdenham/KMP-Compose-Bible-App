package com.martin.bibleapp.ui.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.bible.CurrentReferenceUseCase
import com.martin.bibleapp.domain.bible.ReadPageUseCase
import com.martin.bibleapp.domain.bible.ReferenceSelectionUseCase
import com.martin.bibleapp.domain.bible.TabDocuments
import com.martin.bibleapp.ui.util.ResultIs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.crosswire.jsword.passage.OsisParser
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.versification.system.Versifications

class DocumentViewModel(
    currentReferenceUseCase: CurrentReferenceUseCase,
    private val readPageUseCase: ReadPageUseCase,
    private val referenceSelectionUseCase: ReferenceSelectionUseCase): ViewModel()
{
    val verseFlow = currentReferenceUseCase.getCurrentReferenceFlow()

    private val _documentState = MutableStateFlow<ResultIs<DocumentModel>>(ResultIs.Loading)
    val documentState = _documentState.asStateFlow()

    private val latestState = mutableListOf(TabState(TabDocuments.Document.BSB, Verse.DEFAULT, ""), TabState(TabDocuments.Document.KING_COMMENTS, Verse.DEFAULT, ""))
    private var currentVerse: Verse = Verse.DEFAULT

    private var initialised = false

    init {
        _documentState.value = ResultIs.Loading
        viewModelScope.launch {
            verseFlow.collect { verse ->
                val chapterChange = verse.book != currentVerse.book || verse.chapter != currentVerse.chapter
                currentVerse = verse
                if (!initialised || chapterChange) {
                    loadVerseText(0, TabDocuments.Document.BSB, verse)
                    loadVerseText(1, TabDocuments.Document.KING_COMMENTS, verse)
                    initialised = true
                }
            }
        }
    }

    fun updateVerse(newVerseOsisId: String) {
        println("ZZZZZ updateVerse called with $newVerseOsisId")
        viewModelScope.launch {
            OsisParser().parseOsisID(DEFAULT_VERSIFICATION, newVerseOsisId)?.let { verse ->
                referenceSelectionUseCase.selectVerse(verse)
            }
        }
    }

    fun updateTextColour(html: String, textColour: String): String =
        DocumentHtml.updateTextColour(html, textColour)

    fun pageSelected(page: Int) {
        println("ZZZZZZZ pageSelected called with $page")
        if (!initialised) return
        TabDocuments.tabs[page]?.let { doc ->
            loadVerseText(page, doc, currentVerse)
        }
    }

    private fun loadVerseText(page: Int, document: TabDocuments.Document, verse: Verse?) {
        println("ZZZZZZZ loading verse $verse for document ${document.name}")
        if (verse == null) return

        val v11n = verse.getVersification()
        val tabState = latestState[page]
        val prevDocumentVerse = tabState.verse
        if (prevDocumentVerse == verse && tabState.html.isNotEmpty()) return
        if (document.pageType == TabDocuments.PageType.CHAPTER && v11n.isSameChapter(prevDocumentVerse, verse) && tabState.html.isNotEmpty()) {
            latestState[page] = latestState[page].copy(verse = verse)
            _documentState.value = ResultIs.Success(DocumentModel(latestState.toList()))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            println("ZZZZZZZ REALLY loading verse $verse for document ${document.name}")
            _documentState.value = ResultIs.Loading

            val html = readPageUseCase.readPage(document, verse)

            val htmlPage = DocumentHtml.formatHtmlPage(document.changeVerseOnScroll, html)
            latestState[page] = latestState[page].copy(verse = verse, html = htmlPage)

            withContext(Dispatchers.Main) {
                println("ZZZZZZZ updating UI for document ${document.name}")
                // need to make a copy of latestState or Compose does not know a change occurred
                _documentState.value = ResultIs.Success(DocumentModel(latestState.toList()))
            }
        }
    }

    companion object {
        val DEFAULT_VERSIFICATION = Versifications.instance().getVersification("KJV")
    }
}