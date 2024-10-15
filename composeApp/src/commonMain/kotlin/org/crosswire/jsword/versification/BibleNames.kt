/**
 * Distribution License:
 * JSword is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License, version 2.1 or later
 * as published by the Free Software Foundation. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * The License is available on the internet at:
 * http://www.gnu.org/copyleft/lgpl.html
 * or by writing to:
 * Free Software Foundation, Inc.
 * 59 Temple Place - Suite 330
 * Boston, MA 02111-1307, USA
 *
 * © CrossWire Bible Society, 2005 - 2016
 *
 */
package org.crosswire.jsword.versification

import androidx.compose.ui.text.intl.Locale
import org.crosswire.jsword.book.sword.LocaleHelper.ENGLISH_LOCALE
import org.crosswire.jsword.javautil.ResourceBundle
import org.crosswire.jsword.versification.BibleNames
import org.jetbrains.compose.resources.MissingResourceException
import kotlin.jvm.Transient

//import org.crosswire.common.util.CWClassLoader;
//import org.crosswire.common.util.ClassUtil;
//import org.crosswire.common.util.StringUtil;
//import org.crosswire.jsword.internationalisation.LocaleProviderManager;

/**
 * BibleNames deals with locale sensitive BibleBook name lookup conversions.
 *
 * @author Joe Walker
 * @author DM Smith
 */
class BibleNames private constructor() {
    /**
     * Get the BookName.
     *
     * @param book the desired book
     * @return The requested BookName or null if not in this versification
     */
    fun getBookName(book: BibleBook?): BookName? {
        book ?: return null
        return getLocalizedBibleNames()!!.getBookName(book)
    }

    /**
     * Get the preferred name of a book. Altered by the case setting (see
     * setBookCase() and isFullBookName())
     *
     * @param book the desired book
     * @return The full name of the book or blank if not in this versification
     */
    fun getPreferredName(book: BibleBook): String {
        return getLocalizedBibleNames()!!.getPreferredName(book)
    }

    /**
     * Get the preferred name of a book in specified locale. Altered by the case setting (see
     * setBookCase() and isFullBookName())
     *
     * @param locale the desired locale
     * @param book the desired book
     * @return The full name of the book or blank if not in this versification
     */
    fun getPreferredNameInLocale(book: BibleBook, locale: Locale): String {
        return getBibleNamesForLocale(locale)!!.getPreferredName(book)
    }

    /**
     * Get the full name of a book (e.g. "Genesis"). Altered by the case setting
     * (see setBookCase())
     *
     * @param book the book
     * @return The full name of the book or blank if not in this versification
     */
    fun getLongName(book: BibleBook?): String? {
        book ?: return null
        return getLocalizedBibleNames()!!.getLongName(book)
    }

    /**
     * Get the short name of a book (e.g. "Gen"). Altered by the case setting
     * (see setBookCase())
     *
     * @param book the book
     * @return The short name of the book or blank if not in this versification
     */
    fun getShortName(book: BibleBook?): String? {
        book ?: return null
        return getLocalizedBibleNames()!!.getShortName(book)
    }

    /**
     * Get a book from its name.
     *
     * @param find
     * The string to identify
     * @return The BibleBook, On error null
     */
    fun getBook(find: String): BibleBook? {
        var book: BibleBook? = null
        if (containsLetter(find)) {
            book = BibleBook.fromOSIS(find)

            if (book == null) {
                book = getLocalizedBibleNames()!!.getBook(find, false)
            }

            if (book == null) {
                book = englishBibleNames!!.getBook(find, false)
            }

            if (book == null) {
                book = getLocalizedBibleNames()!!.getBook(find, true)
            }

            if (book == null) {
                book = englishBibleNames!!.getBook(find, true)
            }
        }

        return book
    }

    /**
     * Is the given string a valid book name. If this method returns true then
     * getBook() will return a BibleBook and not null.
     *
     * @param find
     * The string to identify
     * @return true when the book name is recognized
     */
    fun isBook(find: String): Boolean {
        return getBook(find) != null
    }

    /**
     * Load name information for BibleNames for a given locale.
     * This routine is for testing the underlying NameList.
     *
     * @param locale
     */
    fun load(locale: Locale) {
        val bibleNames: NameList = NameList(locale)
        if (localizedBibleNames[locale] == null) {
            localizedBibleNames[locale] = bibleNames
        }
    }

    /**
     *
     * @return the localized bible names
     */
    private fun getLocalizedBibleNames(): NameList? {
        // Get the current Locale
        return getBibleNamesForLocale(Locale.current) //TODO should be Locale.US
    }

    /**
     * Gets the bible names for a specific locale.
     *
     * @param locale the locale
     * @return the bible names for locale
     */
    private fun getBibleNamesForLocale(locale: Locale): NameList? {
        var bibleNames = localizedBibleNames[locale]
        if (bibleNames == null) {
            bibleNames = NameList(locale)
            localizedBibleNames[locale] = bibleNames
        }

        return bibleNames
    }

    /**
     * NameList is the internal, internationalize list of names
     * for a locale.
     *
     * The copyright to this program is held by its authors.
     * @author DM Smith
     */
    private inner class NameList(val locale: Locale) {
        /**
         * The collection of BookNames by BibleBooks.
         */
        private var books: LinkedHashMap<BibleBook, BookName>

        /**
         * The full names of the New Testament books of the Bible normalized,
         * generated at runtime
         */
        private var fullNT: MutableMap<String, BookName>

        /**
         * The full names of the Old Testament books of the Bible normalized,
         * generated at runtime
         */
        private var fullOT: MutableMap<String, BookName>

        /**
         * The full names of the Deuterocanonical books of the Bible normalized,
         * generated at runtime
         */
        private var fullNC: MutableMap<String, BookName>

        /**
         * Standard shortened names for the New Testament books of the Bible,
         * normalized, generated at runtime.
         */
        private var shortNT: MutableMap<String, BookName>

        /**
         * Standard shortened names for the Old Testament books of the Bible
         * normalized, generated at runtime.
         */
        private var shortOT: MutableMap<String, BookName>

        /**
         * Standard shortened names for the Deuterocanonical books of the Bible
         * normalized, generated at runtime.
         */
        private var shortNC: MutableMap<String, BookName>

        /**
         * Alternative shortened names for the New Testament books of the Bible
         * normalized, generated at runtime.
         */
        private var altNT: Map<String, BookName>? = null

        /**
         * Alternative shortened names for the Old Testament books of the Bible
         * normalized, generated at runtime.
         */
        private var altOT: Map<String, BookName>? = null

        /**
         * Alternative shortened names for the Deuterocanonical books of the
         * Bible normalized, generated at runtime.
         */
        private var altNC: Map<String, BookName>? = null

        /**
         * Load up the resources for Bible book and section names, and cache the
         * upper and lower versions of them.
         */
        init {
            var ntCount = 0
            var otCount = 0
            var ncCount = 0
            val bibleBooks = BibleBook.entries.toTypedArray()
            for (book in bibleBooks) {
                val ordinal = book.ordinal
                if (ordinal > BibleBook.INTRO_OT.ordinal && ordinal < BibleBook.INTRO_NT.ordinal) {
                    ++ntCount
                } else if (ordinal > BibleBook.INTRO_NT.ordinal && ordinal <= BibleBook.REV.ordinal) {
                    ++otCount
                } else {
                    ++ncCount
                }
            }

            // Create the book name maps
            books = LinkedHashMap<BibleBook, BookName>(ntCount + otCount + ncCount)

//            val className: String = BibleNames::class.java.getName()
            val shortClassName = "BibleNames" //ClassUtil.getShortClassName(className);
            val resources: ResourceBundle = ResourceBundle()
            //ResourceBundle.getBundle(shortClassName, locale, CWClassLoader.instance(BibleNames.class));

            fullNT = HashMap<String, BookName>(ntCount)
            shortNT = HashMap<String, BookName>(ntCount)
            altNT = HashMap<String, BookName>(ntCount)
            for (i in BibleBook.MATT.ordinal..BibleBook.REV.ordinal) {
                val book = bibleBooks[i]
                store(resources, book, fullNT, shortNT, altNT)
            }

            fullOT = HashMap<String, BookName>(otCount)
            shortOT = HashMap<String, BookName>(otCount)
            altOT = HashMap<String, BookName>(otCount)
            for (i in BibleBook.GEN.ordinal..BibleBook.MAL.ordinal) {
                val book = bibleBooks[i]
                store(resources, book, fullOT, shortOT, altOT)
            }

            fullNC = HashMap<String, BookName>(ncCount)
            shortNC = HashMap<String, BookName>(ncCount)
            altNC = HashMap<String, BookName>(ncCount)
            store(resources, BibleBook.INTRO_BIBLE, fullNC, shortNC, altNC)
            store(resources, BibleBook.INTRO_OT, fullNC, shortNC, altNC)
            store(resources, BibleBook.INTRO_NT, fullNC, shortNC, altNC)
            for (i in BibleBook.REV.ordinal + 1 until bibleBooks.size) {
                val book = bibleBooks[i]
                store(resources, book, fullNC, shortNC, altNC)
            }
        }

        fun getBookName(book: BibleBook): BookName {
            return books.getOrElse(book, { throw IllegalArgumentException("Unknown book") })
        }

        /**
         * Get the preferred name of a book. Altered by the case setting (see
         * setBookCase() and isFullBookName())
         *
         * @param book
         * The book of the Bible
         * @return The full name of the book
         */
        fun getPreferredName(book: BibleBook): String {
            return getBookName(book).preferredName
        }

        /**
         * Get the full name of a book (e.g. "Genesis"). Altered by the case
         * setting (see setBookCase())
         *
         * @param book
         * The book of the Bible
         * @return The full name of the book
         */
        fun getLongName(book: BibleBook): String {
            return getBookName(book).longName
        }

        /**
         * Get the short name of a book (e.g. "Gen"). Altered by the case
         * setting (see setBookCase())
         *
         * @param book
         * The book of the Bible
         * @return The short name of the book
         */
        fun getShortName(book: BibleBook): String {
            return getBookName(book).shortName
        }

        /**
         * Get a book from its name.
         *
         * @param find
         * The string to identify
         * @param fuzzy
         * Whether to also find bible books where only a substring matches
         * @return The BibleBook, On error null
         */
        fun getBook(find: String?, fuzzy: Boolean): BibleBook? {
            val match = BookName.normalize(find!!, locale)

            var bookName = fullNT!![match]
            if (bookName != null) {
                return bookName.book
            }

            bookName = shortNT!![match]
            if (bookName != null) {
                return bookName.book
            }

            bookName = altNT!![match]
            if (bookName != null) {
                return bookName.book
            }

            bookName = fullOT!![match]
            if (bookName != null) {
                return bookName.book
            }

            bookName = shortOT!![match]
            if (bookName != null) {
                return bookName.book
            }

            bookName = altOT!![match]
            if (bookName != null) {
                return bookName.book
            }

            bookName = fullNC!![match]
            if (bookName != null) {
                return bookName.book
            }

            bookName = shortNC!![match]
            if (bookName != null) {
                return bookName.book
            }

            bookName = altNC!![match]
            if (bookName != null) {
                return bookName.book
            }

            if (!fuzzy) {
                return null
            }

            for (aBook in books.values) {
                if (aBook.match(match)) {
                    return aBook.book
                }
            }

            return null
        }

        private fun store(
            resources: ResourceBundle,
            book: BibleBook,
            fullMap: MutableMap<String, BookName>,
            shortMap: MutableMap<String, BookName>,
            altMap: Map<*, *>?
        ) {
            val osisName = book.oSIS

            val fullBook = getString(resources, osisName + Companion.FULL_KEY)

            var shortBook = getString(resources, osisName + Companion.SHORT_KEY)
            if (shortBook.isNullOrEmpty()) {
                shortBook = fullBook
            }

            val altBook = getString(resources, osisName + Companion.ALT_KEY)

            val bookName =
                BookName(locale, BibleBook.fromOSIS(osisName)!!, fullBook!!, shortBook!!, altBook)
            books.put(book, bookName)

            fullMap[bookName.normalizedLongName] = bookName

            shortMap[bookName.normalizedShortName] = bookName

            //            String[] alternates = null; //StringUtil.split(BookName.normalize(altBook, locale), ',');

//            for (int j = 0; j < alternates.length; j++) {
//                altMap.put(alternates[j], bookName);
//            }
        }

        /*
         * Helper to make the code more readable.
         */
        private fun getString(resources: ResourceBundle, key: String): String? {
            try {
                return resources.getString(key);
            } catch (e: MissingResourceException) {

                //assert(false)
            }
            return null
        }
    }

    /** we cache the Localized Bible Names because there is quite a bit of processing going on for each individual Locale  */
    @Transient
    private val localizedBibleNames: MutableMap<Locale, NameList?> = mutableMapOf()

    /**
     * This class is a singleton, enforced by a private constructor.
     */
    init {
        englishBibleNames = getBibleNamesForLocale(ENGLISH_LOCALE)
        localizedBibleNames[ENGLISH_LOCALE] = englishBibleNames
    }

    companion object {
        /**
         * Get the singleton instance of BibleNames.
         *
         * @return the singleton
         */
        fun instance(): BibleNames {
            return instance
        }

        /**
         * This is simply a convenience function to wrap Char.isLetter()
         *
         * @param text
         * The string to be parsed
         * @return true if the string contains letters
         */
        private fun containsLetter(text: String): Boolean {
            text.forEach {
                if (it.isLetter()) {
                    return true
                }
            }

            return false
        }

        /** English BibleNames, or null when using the program's default locale  */
        private var englishBibleNames: NameList? = null

        private val instance = BibleNames()

        private const val FULL_KEY = ".Full"
        private const val SHORT_KEY = ".Short"
        private const val ALT_KEY = ".Alt"
    }
}
