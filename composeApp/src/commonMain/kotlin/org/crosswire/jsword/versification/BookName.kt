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
 * © CrossWire Bible Society, 2007 - 2016
 *
 */
package org.crosswire.jsword.versification

import androidx.compose.ui.text.intl.Locale

//import org.crosswire.common.util.StringUtil;
//import org.crosswire.jsword.book.CaseType;

/**
 * BookName represents the different ways a book of the bible is named.
 *
 */
class BookName(
    locale: Locale,
    /**
     * Get the BibleBook to which this set of names is tied.
     *
     * @return The book
     */
    val book: BibleBook,
    /**
     * Get the full name of a book (e.g. "Genesis"). Altered by the case setting
     * (see setBookCase())
     *
     * @return The full name of the book
     */
    val longName: String, shortName: String, alternateNames: String?
) {
    val preferredName: String
        /**
         * Get the preferred name of a book. Altered by the case setting (see
         * setBookCase() and isFullBookName())
         *
         * @return The preferred name of the book
         */
        get() {
            if (isFullBookName) {
                return longName
            }
            return shortName
        }

    /**
     * Match the normalized name as closely as possible. It will match if:
     *
     *  1. it is a prefix of a normalized alternate name
     *  1. a normalized alternate name is a prefix of it
     *  1. it is a prefix of a normalized long name
     *  1. it is a prefix of a normalized short name
     *  1. a normalized short name is a prefix of it
     *
     *
     * @param normalizedName
     * the already normalized name to match against.
     * @return true of false
     */
    fun match(normalizedName: String): Boolean {
        // Does it match one of the alternative versions
        for (j in alternateNames.indices) {
            val targetBookName = alternateNames[j]
            if (targetBookName.startsWith(normalizedName) || normalizedName.startsWith(
                    targetBookName
                )
            ) {
                return true
            }
        }

        // Does it match a long version of the book
        if (normalizedLongName.startsWith(normalizedName)) {
            return true
        }

        // or a short version
        if (normalizedShortName.startsWith(normalizedName) || (normalizedShortName.length > 0 && normalizedName.startsWith(
                normalizedShortName
            ))
        ) {
            return true
        }

        return false
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    override fun hashCode(): Int {
        return book.hashCode()
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null) {
            return false
        }

        if (other !is BookName) {
            return false
        }

        return book == other.book
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return preferredName
    }

    //
//        if (caseType == CaseType.LOWER) {
//            return longName.toLowerCase(locale);
//        }
//
//        if (caseType == CaseType.UPPER) {
//            return longName.toUpperCase(locale);
//        }

    /**
     * @return the normalizedLongName
     */
    val normalizedLongName: String//        CaseType caseType = BookName.getDefaultCase();
//
//        if (caseType == CaseType.LOWER) {
//            return shortName.toLowerCase(locale);
//        }
//
//        if (caseType == CaseType.UPPER) {
//            return shortName.toUpperCase(locale);
//        }
//
    /**
     * Get the short name of a book (e.g. "Gen"). Altered by the case setting
     * (see setBookCase())
     *
     * @return The short name of the book
     */
    val shortName: String

    /**
     * @return the normalizedShortName
     */
    val normalizedShortName: String
    private val alternateNames: Array<String> = arrayOf()

    /**
     * Create a BookName for a Book of the Bible in a given language.
     *
     * @param locale
     * the language of this BookName
     * @param book
     * the Book's canonical number
     * @param longName
     * the Book's long name
     * @param shortName
     * the Book's short name, if any
     * @param alternateNames
     * optional comma separated list of alternates for the Book
     */
    init {
        this.normalizedLongName = normalize(longName, locale)
        this.shortName = shortName
        this.normalizedShortName = normalize(shortName, locale)

        //        if (alternateNames != null) {
//            this.alternateNames = StringUtil.split(normalize(alternateNames, locale), ',');
//        }
    }

    companion object {
        /**
         * Normalize by stripping punctuation and whitespace and lowercasing.
         *
         * @param str
         * the string to normalize
         * @param locale the locale that should be used for normalization
         * @return the normalized string
         */
        fun normalize(str: String, locale: Locale = Locale.current): String {
            return normPattern.replace(str,"").lowercase() //TODO what about locale?
        }

        /**
         * This is only used by config.
         *
         * @param bookCase
         * The new case to use for reporting book names
         * @exception IllegalArgumentException
         * If the case is not between 0 and 2
         * @see .getCase
         */
        //    public static void setCase(int bookCase) {
        //        BookName.bookCase = CaseType.fromInteger(bookCase);
        //    }
        /**
         * How do we report the names of the books?. These are static. This is on
         * the assumption that we will not want to have different sections of the
         * app using a different format. I expect this to be a good assumption, and
         * it saves passing a Book class around everywhere. CaseType.MIXED is not
         * allowed
         *
         * @param newBookCase
         * The new case to use for reporting book names
         * @exception IllegalArgumentException
         * If the case is not between 0 and 2
         * @see .getCase
         */
        //    public static void setCase(CaseType newBookCase) {
        //        BookName.bookCase = newBookCase;
        //    }
        /**
         * This is only used by config
         *
         * @return The current case setting
         */
        //    public static int getCase() {
        //        return BookName.bookCase.toInteger();
        //    }

        /**
         * How do we report the names of the books?.
         *
         * @return The current case setting
         * @see .setCase
         */
        //    public static CaseType getDefaultCase() {
        //        return BookName.bookCase;
        //    }
        /** remove spaces and some punctuation in Book Name (make sure , is allowed)  */
        private val normPattern: Regex = "[. ]".toRegex()

        /** How the book names are reported.  */ //    private static CaseType bookCase = CaseType.SENTENCE;
        /**
         * This is only used by config
         *
         * @return Whether the name is long or short. Default is Full (true).
         * @see .setFullBookName
         */
        /**
         * Set whether the name should be full or abbreviated, long or short.
         *
         * @param fullName
         * The new case to use for reporting book names
         * @see .isFullBookName
         */
        /** Whether long or short, full or abbreviated names are used.  */
        var isFullBookName: Boolean = true
    }
}
