package org.crosswire.jsword.book.sword

import org.crosswire.jsword.book.Book
import org.crosswire.jsword.passage.Key
import org.crosswire.jsword.passage.KeyText

class SwordBook(val swordBookMetaData: SwordBookMetaData, val backend: Backend<*>): Book {

    override fun readToOsis(key: Key): List<KeyText> = backend.readToOsis(key)

    override fun getRawText(key: Key): String = backend.getRawText(key)
}