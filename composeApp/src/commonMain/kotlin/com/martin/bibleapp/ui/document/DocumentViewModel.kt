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
import org.crosswire.jsword.passage.VerseRange
import org.crosswire.jsword.versification.system.Versifications

class DocumentViewModel(
    currentReferenceUseCase: CurrentReferenceUseCase,
    private val readPageUseCase: ReadPageUseCase,
    private val referenceSelectionUseCase: ReferenceSelectionUseCase): ViewModel()
{
    private val _documentState = MutableStateFlow<ResultIs<DocumentModel>>(ResultIs.Loading)
    val documentState = _documentState.asStateFlow()

    private val latestTabsConfig = mutableListOf(
        TabConfig(TabDocuments.Document.BSB, Verse.DEFAULT, "", DEFAULT_VERSE_RANGE),
        TabConfig(TabDocuments.Document.KING_COMMENTS, Verse.DEFAULT, "", DEFAULT_VERSE_RANGE)
    )
    private var currentPageNo = 0
    private var currentVerse: Verse = Verse.DEFAULT

    private var initialised = false

    init {
        _documentState.value = ResultIs.Loading
        viewModelScope.launch {
            currentReferenceUseCase.getCurrentReferenceFlow().collect { verse ->
                val tabState = latestTabsConfig[currentPageNo]
                if (!initialised || !tabState.pageVerseRange.contains(verse)) {
                    currentVerse = verse
                    loadVerseText(0, TabDocuments.Document.BSB, verse)
                    loadVerseText(1, TabDocuments.Document.KING_COMMENTS, verse)
                    initialised = true
                }
            }
        }
    }

    fun updateVerse(newVerseOsisId: String) {
        viewModelScope.launch {
            OsisParser().parseOsisID(DEFAULT_VERSIFICATION, newVerseOsisId)?.let { verse ->
                referenceSelectionUseCase.selectVerse(verse)
            }
        }
    }

    fun updateTextColour(html: String, textColour: String): String =
        DocumentHtml.updateTextColour(html, textColour)

    fun pageSelected(page: Int) {
        if (!initialised) return
        currentPageNo = page
        TabDocuments.tabs[page]?.let { doc ->
            loadVerseText(page, doc, currentVerse)
        }
    }

    private fun loadVerseText(page: Int, document: TabDocuments.Document, verse: Verse?) {
        if (verse == null) return

        val tabState = latestTabsConfig[page]
        val prevDocumentVerseRange = tabState.pageVerseRange
        if (prevDocumentVerseRange.contains(verse) && tabState.html.isNotEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            _documentState.value = ResultIs.Loading

            val verseRangeToRead = readPageUseCase.calculatePageVerseRange(document, verse)
            val html = readPageUseCase.readPage(document, verseRangeToRead)

            val htmlPage = DocumentHtml.formatHtmlPage(document.changeVerseOnScroll, html)
            latestTabsConfig[page] = latestTabsConfig[page].copy(verse = verse, html = htmlPage, pageVerseRange = verseRangeToRead)

            withContext(Dispatchers.Main) {
                // need to make a copy of latestState or Compose does not know a change occurred
                _documentState.value = ResultIs.Success(DocumentModel(latestTabsConfig.toList()))
            }
        }
    }

    companion object {
        val DEFAULT_VERSIFICATION = Versifications.instance().getVersification("KJV")
        val DEFAULT_VERSE_RANGE = VerseRange(Verse.DEFAULT.getVersification(), Verse.DEFAULT, Verse.DEFAULT)
    }
}