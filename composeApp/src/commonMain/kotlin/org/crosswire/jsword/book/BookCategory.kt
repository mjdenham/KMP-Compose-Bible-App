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
package org.crosswire.jsword.book

import org.crosswire.jsword.JSMsg.gettext

/**
 * An Enumeration of the possible types of Book.
 *
 * @see gnu.lgpl.License The GNU Lesser General Public License for details.
 *
 * @author Joe Walker
 * @author DM Smith
 */
enum class BookCategory
/**
 * @param name
 * The name of the BookCategory
 * @param externalName the name of the BookCategory worthy of an end user
 */(
    /**
     * The names of the BookCategory
     */
    val fullName: String, private val externalName: String
) {
    /** Books that are Bibles  */ // TRANSLATOR: The name for the book category consisting of Bibles.
    BIBLE("Biblical Texts", gettext("Biblical Texts")),

    /** Books that are Dictionaries  */ // TRANSLATOR: The name for the book category consisting of Lexicons and Dictionaries.
    DICTIONARY("Lexicons / Dictionaries", gettext("Dictionaries")),

    /** Books that are Commentaries  */ // TRANSLATOR: The name for the book category consisting of Commentaries.
    COMMENTARY("Commentaries", gettext("Commentaries")),

    /** Books that are indexed by day. AKA, Daily Devotions  */ // TRANSLATOR: The name for the book category consisting of Daily Devotions, indexed by day of the year.
    DAILY_DEVOTIONS("Daily Devotional", gettext("Daily Devotionals")),

    /** Books that map words from one language to another.  */ // TRANSLATOR: The name for the book category consisting of Glossaries that map words/phrases from one language into another.
    GLOSSARY("Glossaries", gettext("Glossaries")),

    /** Books that are questionable.  */ // TRANSLATOR: The name for the book category consisting of books that are considered unorthodox by mainstream Christianity.
    QUESTIONABLE(
        "Cults / Unorthodox / Questionable Material",
        gettext("Cults / Unorthodox / Questionable Materials")
    ),

    /** Books that are just essays.  */ // TRANSLATOR: The name for the book category consisting of just essays.
    ESSAYS("Essays", gettext("Essays")),

    /** Books that are predominately images.  */ // TRANSLATOR: The name for the book category consisting of books containing mostly images.
    IMAGES("Images", gettext("Images")),

    /** Books that are a collection of maps.  */ // TRANSLATOR: The name for the book category consisting of books containing mostly maps.
    MAPS("Maps", gettext("Maps")),

    /** Books that are just books.  */ // TRANSLATOR: The name for the book category consisting of general books.
    GENERAL_BOOK("Generic Books", gettext("General Books")),

    /** Books that are not any of the above. This is a catch all for new book categories.  */ // TRANSLATOR: The name for the book category consisting of books not in any of the other categories.
    OTHER("Other", gettext("Other"));

    /**
     * @return the internationalized name.
     */
    override fun toString(): String {
        return externalName
    }

    companion object {
        /**
         * Lookup method to convert from a String
         *
         * @param name the internal name of a BookCategory
         * @return the matching BookCategory
         */
        fun fromString(fullName: String): BookCategory {
            for (o in entries) {
                if (o.fullName.equals(fullName, ignoreCase = true) || o.name.equals(fullName, ignoreCase = true)) {
                    return o
                }
            }
            return OTHER
        }

        /**
         * Lookup method to convert from a String
         *
         * @param name the external name of a BookCategory
         * @return the matching BookCategory
         */
        fun fromExternalString(name: String?): BookCategory {
            for (o in entries) {
                if (o.externalName.equals(name, ignoreCase = true)) {
                    return o
                }
            }
            return OTHER
        }

        /**
         * Lookup method to convert from an integer
         *
         * @param i the ordinal value of the BookCategory in this enumeration.
         * @return the i-th BookCategory
         */
        fun fromInteger(i: Int): BookCategory {
            for (o in entries) {
                if (i == o.ordinal) {
                    return o
                }
            }
            return OTHER
        }
    }
}
