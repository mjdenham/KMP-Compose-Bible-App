package com.martin.bibleapp.ui.document

import com.martin.bibleapp.domain.bible.TabDocuments
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.passage.VerseRange

data class DocumentModel(val tabStateList: List<TabState>)

data class TabState(val document: TabDocuments.Document, val verse: Verse, val html: String, val pageVerseRange: VerseRange)