package com.martin.bibleapp.ui.selector

import androidx.lifecycle.ViewModel
import org.crosswire.jsword.versification.BibleBook
import org.crosswire.jsword.versification.Versification
import org.crosswire.jsword.versification.system.SystemDefault.BOOKS_NT
import org.crosswire.jsword.versification.system.SystemDefault.BOOKS_OT
import org.crosswire.jsword.versification.system.Versifications

class BookSelectorViewModel(): ViewModel() {
    fun getBooks(): List<BibleBook> {
        val v11nName = "KJV"
        val v11n: Versification = Versifications.instance().getVersification(v11nName)
        return (BOOKS_OT + BOOKS_NT).toList().filter {
            v11n.containsBook(it)
        }
    }
}