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
 * © CrossWire Bible Society, 2012 - 2016
 *
 */
package org.crosswire.jsword.versification

/**
 * A BibleBookList is an ordered list of one or more BibleBooks.
 * Typically, a BibleBookList is a member of a Versification.
 *
 * @author DM Smith
 */
/* pkg protected */
internal class BibleBookList(val books: Array<BibleBook>) : Iterable<BibleBook?> /*, java.io.Serializable */ {
    /**
     * Does this Versification contain the BibleBook.
     *
     * @param book
     * @return true if it is present.
     */
    fun contains(book: BibleBook?): Boolean {
        return book != null && bookMap[book.ordinal] != -1
    }

    /**
     * Where does the BibleBook come in the order of books of the Bible.
     * The first book is 0, the next is 1 and so forth.
     * If the BibleBook is not in this Reference System,
     * then the return value of this routine is -1.
     *
     * @param book
     * @return the ordinal value of the book or -1 if not present
     */
    fun getOrdinal(book: BibleBook?): Int {
        book ?: return -1
        return bookMap[book.ordinal]
    }

    val bookCount: Int
        /**
         * Get the number of books in this Versification.
         * @return the number of books
         */
        get() = books.size

    /**
     * Get the BibleBook by its position in this Versification.
     * If the position is negative, return the first book.
     * If the position is greater than the last, return the last book.
     *
     * @param ordinal
     * @return the indicated book
     */
    fun getBook(ordinal: Int): BibleBook {
        var ord = ordinal
        if (ord < 0) {
            ord = 0
        }
        if (ord >= books.size) {
            ord = books.size - 1
        }
        return books[ord]
    }

    /**
     * Get the BibleBooks in this Versification.
     *
     * @return an Iterator over the books
     */
    override fun iterator(): Iterator<BibleBook> {
        return object : MutableIterator<BibleBook> {
            private var nextBook: BibleBook? = books[0]

            override fun hasNext(): Boolean {
                return nextBook != null
            }

            override fun next(): BibleBook {
                nextBook?.let {
                    val current: BibleBook = it
                    nextBook = getNextBook(it)
                    return current

                } ?: throw NoSuchElementException()
            }

            override fun remove() {
                throw UnsupportedOperationException()
            }
        }
    }

    val firstBook: BibleBook
        /**
         * Return the first book in the list.
         *
         * @return the first book in the list
         */
        get() = books[0]

    val lastBook: BibleBook
        /**
         * Return the first book in the list.
         *
         * @return the first book in the list
         */
        get() = books[books.size - 1]

    /**
     * Given a BibleBook, get the previous BibleBook in this Versification. If it is the first book, return null.
     * @param book A BibleBook in the Versification
     * @return the previous BibleBook or null.
     */
    fun getPreviousBook(book: BibleBook?): BibleBook? {
        if (book == null) {
            return null
        }
        val ordinal = book.ordinal
        val position = bookMap[ordinal]
        if (position > 0) {
            return books[position - 1]
        }

        return null
    }

    /**
     * Given a BibleBook, get the next BibleBook in this Versification. If it is the last book, return null.
     * @param book A BibleBook in the Versification
     * @return the previous BibleBook or null.
     */
    fun getNextBook(book: BibleBook?): BibleBook? {
        if (book == null) {
            return null
        }
        val ordinal = book.ordinal
        val position = bookMap[ordinal]
        if (position != -1 && position + 1 < books.size) {
            return books[position + 1]
        }
        return null
    }

    /** The bookMap maps from a BibleBook to the position that it has in `books`.  */
    private var bookMap: IntArray

    /**
     * Create an ordered BibleBookList from the input.
     * @param books
     */
    init {
        bookMap = IntArray(BibleBook.entries.size + 1)
        // Initialize all slots to -1
        for (b in BibleBook.entries) {
            bookMap[b.ordinal] = -1
        }
        // Fill in the position of the books into that list
        for (i in books.indices) {
            bookMap[books[i].ordinal] = i
        }
    }

    companion object {
        /**
         * Serialization ID
         */
        private const val serialVersionUID = -2681289798451902815L
    }
}
