/**
 * Distribution License:
 * JSword is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License, version 2.1 as published by
 * the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * The License is available on the internet at:
 * http://www.gnu.org/copyleft/lgpl.html
 * or by writing to:
 * Free Software Foundation, Inc.
 * 59 Temple Place - Suite 330
 * Boston, MA 02111-1307, USA
 *
 * Copyright: 2005
 * The copyright to this program is held by it's authors.
 *
 * ID: $Id$
 */
package com.martin.kmpsword.versification

/**
 * BibleNames is a static class that deals with Book name lookup conversions. We
 * start counting at 1 for books (so Genesis=1, Revelation=66). However
 * internally books start counting at 0 and go up to 65.
 *
 * @see gnu.lgpl.License for license details.<br></br>
 * The copyright to this program is held by it's authors.
 *
 * @author Joe Walker [joe at eireneh dot com]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
class BibleNames {
//class BibleNames(locale: Locale) {
//    fun getName(book: Int): BookName? {
//        try {
//            return books[book - 1]
//        } catch (ex: IndexOutOfBoundsException) {
//            // This is faster than doing the check explicitly, unless
//            // The exception is actually thrown, then it is a lot slower
//            // I'd like to think that the norm is to get it right
//            throw NoSuchVerseException(Msg.BOOKS_BOOK, arrayOf<Any>(book))
//        }
//    }
//
//    /**
//     * Get the preferred name of a book. Altered by the case setting (see
//     * setBookCase() and isLongBookName())
//     *
//     * @param book
//     * The book number (1-66)
//     * @return The full name of the book
//     * @exception NoSuchVerseException
//     * If the book number is not valid
//     */
//    @Throws(NoSuchVerseException::class)
//    fun getPreferredName(book: Int): String {
//        return getName(book).getPreferredName()
//    }
//
//    /**
//     * Get the full name of a book (e.g. "Genesis"). Altered by the case setting
//     * (see setBookCase())
//     *
//     * @param book
//     * The book number (1-66)
//     * @return The full name of the book
//     * @exception NoSuchVerseException
//     * If the book number is not valid
//     */
//    @Throws(NoSuchVerseException::class)
//    fun getLongName(book: Int): String {
//        return getName(book).getLongName()
//    }
//
//    /**
//     * Get the short name of a book (e.g. "Gen"). Altered by the case setting
//     * (see setBookCase())
//     *
//     * @param book
//     * The book number (1-66)
//     * @return The short name of the book
//     * @exception NoSuchVerseException
//     * If the book number is not valid
//     */
//    @Throws(NoSuchVerseException::class)
//    fun getShortName(book: Int): String {
//        return getName(book).getShortName()
//    }
//
//    /**
//     * Get number of a book from its name.
//     *
//     * @param find
//     * The string to identify
//     * @return The book number (1 to 66) On error -1
//     */
//    fun getNumber(find: String?): Int {
//        val match: String = BookName.normalize(find, locale)
//
//        var bookName: BookName? = fullBooksMap!![match] as BookName?
//        if (bookName != null) {
//            return bookName.getNumber()
//        }
//
//        bookName = shortBooksMap!![match] as BookName?
//        if (bookName != null) {
//            return bookName.getNumber()
//        }
//
//        bookName = altBooksMap!![match] as BookName?
//        if (bookName != null) {
//            return bookName.getNumber()
//        }
//
//        for (i in books.indices) {
//            bookName = books[i]
//            if (bookName.match(match)) {
//                return bookName.getNumber()
//            }
//        }
//
//        return -1
//    }
//
//    /**
//     * Is the given string a valid book name. If this method returns true then
//     * getBookNumber() will return a number and not throw an exception.
//     *
//     * @param find
//     * The string to identify
//     * @return The book number (1 to 66)
//     */
//    fun isBookName(find: String?): Boolean {
//        return getNumber(find) != -1
//    }
//
//    /**
//     * Load up the resources for Bible book and section names, and cache the
//     * upper and lower versions of them.
//     */
//    private fun initialize() {
//        val booksInBible: Int = BibleInfo.booksInBible()
//
//        books = arrayOf<BookName>(booksInBible)
//
//        // Create the book name maps
//        fullBooksMap = HashMap(booksInBible)
//        shortBooksMap = HashMap(booksInBible)
//
//        altBooksMap = HashMap(booksInBible)

//        val resources: java.util.ResourceBundle = java.util.ResourceBundle.getBundle(
//            BibleNames::class.java.getName(), locale, CWClassLoader.instance(
//                BibleNames::class.java
//            )
//        )

//        for (i in 0 until booksInBible) {
//            var osisName = "" //$NON-NLS-1$
//            try {
//                osisName = OSISNames.getName(i + 1)
//            } catch (e: NoSuchVerseException) {
//                assert(false)
//            }
//
//            val fullBook = getString(resources, osisName + FULL_KEY)
//
//            var shortBook = getString(resources, osisName + SHORT_KEY)
//            if (shortBook!!.length == 0) {
//                shortBook = fullBook
//            }
//
//            val altBook = getString(resources, osisName + ALT_KEY)
//
//            val bookName: BookName = BookName(locale, i + 1, fullBook, shortBook, altBook)
//            books[i] = bookName
//
//            fullBooksMap!![bookName.getNormalizedLongName()] = bookName
//
//            shortBooksMap!![bookName.getNormalizedShortName()] = bookName
//
//            val alternates: Array<String> = StringUtil.split(altBook, ',')
//
//            for (j in alternates.indices) {
//                altBooksMap!![alternates[j]] = bookName
//            }
//        }
//    }

//    /*
//     * Helper to make the code more readable.
//     */
//    private fun getString(resources: java.util.ResourceBundle, key: String): String? {
//        try {
//            return resources.getString(key)
//        } catch (e: java.util.MissingResourceException) {
//            assert(false)
//        }
//        return null
//    }
//
//    private var books: Array<BookName>
//
//    /** The locale for the Bible Names  */
//    private val locale: Locale = locale
//
//    /**
//     * The full names of the book of the Bible, normalized, generated at runtime
//     */
//    private lateinit var fullBooksMap: MutableMap<String, BookName>
//
//    /**
//     * Standard shortened names for the book of the Bible, normalized, generated
//     * at runtime.
//     */
//    private lateinit var shortBooksMap: MutableMap<String, BookName>
//
//    /**
//     * Alternative shortened names for the book of the Bible, normalized,
//     * generated at runtime
//     */
//    private lateinit var altBooksMap: MutableMap<String, BookName>
//
//    /**
//     * Create BibleNames for the given locale
//     */
//    init {
//        initialize()
//    }

    companion object {
        private const val FULL_KEY = ".Full" //$NON-NLS-1$
        private const val SHORT_KEY = ".Short" //$NON-NLS-1$
        private const val ALT_KEY = ".Alt" //$NON-NLS-1$

        /**
         * Handy book finder
         */
        const val GENESIS: Byte = 1
        const val EXODUS: Byte = 2
        const val LEVITICUS: Byte = 3
        const val NUMBERS: Byte = 4
        const val DEUTERONOMY: Byte = 5
        const val JOSHUA: Byte = 6
        const val JUDGES: Byte = 7
        const val RUTH: Byte = 8
        const val SAMUEL1: Byte = 9
        const val SAMUEL2: Byte = 10
        const val KINGS1: Byte = 11
        const val KINGS2: Byte = 12
        const val CHRONICLES1: Byte = 13
        const val CHRONICLES2: Byte = 14
        const val EZRA: Byte = 15
        const val NEHEMIAH: Byte = 16
        const val ESTHER: Byte = 17
        const val JOB: Byte = 18
        const val PSALMS: Byte = 19
        const val PROVERBS: Byte = 20
        const val ECCLESIASTES: Byte = 21
        const val SONGOFSOLOMON: Byte = 22
        const val ISAIAH: Byte = 23
        const val JEREMIAH: Byte = 24
        const val LAMENTATIONS: Byte = 25
        const val EZEKIEL: Byte = 26
        const val DANIEL: Byte = 27
        const val HOSEA: Byte = 28
        const val JOEL: Byte = 29
        const val AMOS: Byte = 30
        const val OBADIAH: Byte = 31
        const val JONAH: Byte = 32
        const val MICAH: Byte = 33
        const val NAHUM: Byte = 34
        const val HABAKKUK: Byte = 35
        const val ZEPHANIAH: Byte = 36
        const val HAGGAI: Byte = 37
        const val ZECHARIAH: Byte = 38
        const val MALACHI: Byte = 39
        const val MATTHEW: Byte = 40
        const val MARK: Byte = 41
        const val LUKE: Byte = 42
        const val JOHN: Byte = 43
        const val ACTS: Byte = 44
        const val ROMANS: Byte = 45
        const val CORINTHIANS1: Byte = 46
        const val CORINTHIANS2: Byte = 47
        const val GALATIANS: Byte = 48
        const val EPHESIANS: Byte = 49
        const val PHILIPPIANS: Byte = 50
        const val COLOSSIANS: Byte = 51
        const val THESSALONIANS1: Byte = 52
        const val THESSALONIANS2: Byte = 53
        const val TIMOTHY1: Byte = 54
        const val TIMOTHY2: Byte = 55
        const val TITUS: Byte = 56
        const val PHILEMON: Byte = 57
        const val HEBREWS: Byte = 58
        const val JAMES: Byte = 59
        const val PETER1: Byte = 60
        const val PETER2: Byte = 61
        const val JOHN1: Byte = 62
        const val JOHN2: Byte = 63
        const val JOHN3: Byte = 64
        const val JUDE: Byte = 65
        const val REVELATION: Byte = 66
    }
}
