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
package org.crosswire.jsword.passage

import org.crosswire.jsword.versification.Versification
import kotlin.jvm.JvmOverloads
import kotlin.jvm.Transient

/**
 * A VerseRange is one step between a Verse and a Passage - it is a Verse plus a
 * verseCount. Every VerseRange has a start, a verseCount and an end. A
 * VerseRange is designed to be immutable. This is a necessary from a
 * collections point of view. A VerseRange should always be valid, although some
 * versions may not return any text for verses that they consider to be
 * miss-translated in some way.
 *
 * @see gnu.lgpl.License The GNU Lesser General Public License for details.
 *
 * @author Joe Walker
 * @author DM Smith
 */
class VerseRange : VerseKey<VerseRange?> {
    /**
     * The default VerseRange is a single verse - Genesis 1:1. I didn't want to
     * provide this constructor however, you are supposed to provide a default
     * ctor for all beans. For this reason I suggest you don't use it.
     *
     * @param v11n
     * The versification for the range
     */
    @JvmOverloads
    constructor(v11n: Versification, start: Verse = Verse.DEFAULT, end: Verse = start) {
        checkNotNull(v11n)
        checkNotNull(start)
        checkNotNull(end)

        this.v11n = v11n
//        shaper = NumberShaper()

        val distance: Int = v11n.distance(start, end)

        if (distance < 0) {
            this.start = end
            this.end = start
            this.verseCount = calcVerseCount()
        } else if (distance == 0) {
            this.start = start
            this.end = start
            this.verseCount = 1
        } else {
            this.start = start
            this.end = end
            this.verseCount = calcVerseCount()
        }
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.VerseKey#getVersification()
     */
    override fun getVersification(): Versification {
        return v11n
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.VerseKey#reversify(org.crosswire.jsword.versification.Versification)
     */
    fun reversify(newVersification: Versification): VerseRange? {
        if (v11n == newVersification) {
            return this
        }
        val newStart = start.reversify(newVersification) ?: return null
        val newEnd = end.reversify(newVersification) ?: return null
        return VerseRange(newVersification, newStart, newEnd)
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.VerseKey#isWhole()
     */
    override fun isWhole(): Boolean {
        return start.isWhole() && end.isWhole()
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.VerseKey#getWhole()
     */
    override fun getWhole(): VerseRange {
        if (isWhole()) {
            return this
        }
        return VerseRange(v11n, start.getWhole(), end.getWhole())
    }

    /**
     * Merge 2 VerseRanges together. The resulting range will encompass
     * Everything in-between the extremities of the 2 ranges.
     *
     * @param a
     * The first verse range to be merged
     * @param b
     * The second verse range to be merged
     */
    constructor(a: VerseRange, b: VerseRange) {
        v11n = a.v11n
//        shaper = NumberShaper()
        start = v11n.min(a.start, b.start)
        end = v11n.max(a.end, b.end)
        verseCount = calcVerseCount()
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#getName()
     */
    override fun getName(): String? {
        return getName(null)
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#getName(org.crosswire.jsword.passage.Key)
     */
    override fun getName(base: Key?): String? {
        if (PassageUtil.isPersistentNaming && originalName != null) {
            return originalName
        }

        val rangeName = doGetName(base)
        // Only shape it if it can be unshaped.
//        if (shaper.canUnshape()) {
//            return shaper.shape(rangeName)
//        }

        return rangeName
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#getRootName()
     */
    override fun getRootName(): String? {
        return start.getRootName()
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#getOsisRef()
     */
    override fun getOsisRef(): String? {
        val startBook = start.book
        val endBook = end.book
        val startChapter = start.chapter
        val endChapter = end.chapter

        // If this is in 2 separate books
        if (startBook != endBook) {
            val buf: StringBuilder = StringBuilder()
            if (v11n.isStartOfBook(start)) {
                buf.append(startBook.oSIS)
            } else if (v11n.isStartOfChapter(start)) {
                buf.append(startBook.oSIS)
                buf.append(Verse.VERSE_OSIS_DELIM)
                buf.append(startChapter)
            } else {
                buf.append(start.getOsisRef())
            }

            buf.append(RANGE_PREF_DELIM)

            if (v11n.isEndOfBook(end)) {
                buf.append(endBook.oSIS)
            } else if (v11n.isEndOfChapter(end)) {
                buf.append(endBook.oSIS)
                buf.append(Verse.VERSE_OSIS_DELIM)
                buf.append(endChapter)
            } else {
                buf.append(end.getOsisRef())
            }

            return buf.toString()
        }

        // This range is exactly a whole book
        if (isWholeBook) {
            // Just report the name of the book, we don't need to worry
            // about the
            // base since we start at the start of a book, and should have
            // been
            // recently normalized()
            return startBook.oSIS
        }

        // If this is 2 separate chapters in the same book
        if (startChapter != endChapter) {
            val buf = StringBuilder()
            if (v11n.isStartOfChapter(start)) {
                buf.append(startBook.oSIS)
                buf.append(Verse.VERSE_OSIS_DELIM)
                buf.append(startChapter)
            } else {
                buf.append(start.getOsisRef())
            }

            buf.append(RANGE_PREF_DELIM)

            if (v11n.isEndOfChapter(end)) {
                buf.append(endBook.oSIS)
                buf.append(Verse.VERSE_OSIS_DELIM)
                buf.append(endChapter)
            } else {
                buf.append(end.getOsisRef())
            }

            return buf.toString()
        }

        // If this range is exactly a whole chapter
        if (isWholeChapter) {
            // Just report the name of the book and the chapter
            val buf = StringBuilder()
            buf.append(startBook.oSIS)
            buf.append(Verse.VERSE_OSIS_DELIM)
            buf.append(startChapter)
            return buf.toString()
        }

        // If this is 2 separate verses
        if (start.verse != end.verse) {
            val buf = StringBuilder()
            buf.append(start.getOsisRef())
            buf.append(RANGE_PREF_DELIM)
            buf.append(end.getOsisRef())
            return buf.toString()
        }

        // The range is a single verse
        return start.getOsisRef()
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#getOsisID()
     */
    override fun getOsisID(): String? {
        // This range is exactly a whole book

        if (isWholeBook) {
            // Just report the name of the book, we don't need to worry
            // about the base since we start at the start of a book, and
            // should have been recently normalized()
            return start.book.oSIS
        }

        // If this range is exactly a whole chapter
        if (isWholeChapter) {
            // Just report the name of the book and the chapter
            return start.book.oSIS + Verse.VERSE_OSIS_DELIM + start.chapter
        }

        val startOrdinal = start.ordinal
        val endOrdinal = end.ordinal

        // to see if it is wholly contained in the range and output it if it is.

        // Estimate the size of the buffer: book.dd.dd (where book is 3-5, 3 typical)
        val buf: StringBuilder =
            StringBuilder((endOrdinal - startOrdinal + 1) * 10)
        buf.append(start.getOsisID())
        for (i in startOrdinal + 1 until endOrdinal) {
            buf.append(AbstractPassage.REF_OSIS_DELIM)
            buf.append(v11n.decodeOrdinal(i).getOsisID())
        }

        // It just might be a single verse range!
        if (startOrdinal != endOrdinal) {
            buf.append(AbstractPassage.REF_OSIS_DELIM)
            buf.append(end.getOsisID())
        }

        return buf.toString()
    }

    override fun toString(): String {
        return getName()!!
    }

    override fun clone(): VerseRange {
        //TODO
        // This gets us a shallow copy
//        var copy: VerseRange? = null
//        try {
//            copy = super.clone() as VerseRange?
//            copy!!.start = start
//            copy.end = end
//            copy.verseCount = verseCount
//            copy.originalName = originalName
//            copy.shaper = NumberShaper()
//            copy.v11n = v11n
//        } catch (e: java.lang.CloneNotSupportedException) {
//            assert(false) { e }
//        }
//
//        return copy
        return this
    }

    override fun equals(obj: Any?): Boolean {
        if (obj !is VerseRange) {
            return false
        }
        val vr = obj
        return verseCount == vr.verseCount && start.equals(vr.start) && v11n == vr.v11n
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + verseCount
        return 31 * result + (if ((v11n == null)) 0 else v11n.hashCode())
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    override fun compareTo(obj: Key): Int {
        val that = obj as VerseRange

        val result = start.compareTo(that.start)
        return if (result == 0) this.verseCount - that.verseCount else result
    }

    /**
     * Are the 2 VerseRanges in question contiguous. that is - could they be
     * represented by a single VerseRange. Note that one range could be entirely
     * contained within the other and they would be considered adjacentTo() For
     * example Gen 1:1-2 is adjacent to Gen 1:1-5 and Gen 1:3-4 but not to Gen
     * 1:4-10. Also Gen 1:29-30 is adjacent to Gen 2:1-10
     *
     * @param that
     * The VerseRange to compare to
     * @return true if the ranges are adjacent
     */
    fun adjacentTo(that: VerseRange): Boolean {
        val thatStart = that.start.ordinal
        val thatEnd = that.end.ordinal
        val thisStart = start.ordinal
        val thisEnd = end.ordinal

        // if that starts inside or is next to this we are adjacent.
        if (thatStart >= thisStart - 1 && thatStart <= thisEnd + 1) {
            return true
        }

        // if this starts inside or is next to that we are adjacent.
        if (thisStart >= thatStart - 1 && thisStart <= thatEnd + 1) {
            return true
        }

        // otherwise we're not adjacent
        return false
    }

    /**
     * Do the 2 VerseRanges in question actually overlap. This is slightly more
     * restrictive than the adjacentTo() test which could be satisfied by ranges
     * like Gen 1:1-2 and Gen 1:3-4. overlaps() however would return false given
     * these ranges. For example Gen 1:1-2 is adjacent to Gen 1:1-5 but not to
     * Gen 1:3-4 not to Gen 1:4-10. Also Gen 1:29-30 does not overlap Gen 2:1-10
     *
     * @param that
     * The VerseRange to compare to
     * @return true if the ranges are adjacent
     */
    fun overlaps(that: VerseRange): Boolean {
        val thatStart = that.start.ordinal
        val thatEnd = that.end.ordinal
        val thisStart = start.ordinal
        val thisEnd = end.ordinal

        // if that starts inside this we are adjacent.
        if (thatStart >= thisStart && thatStart <= thisEnd) {
            return true
        }

        // if this starts inside that we are adjacent.
        if (thisStart >= thatStart && thisStart <= thatEnd) {
            return true
        }

        // otherwise we're not adjacent
        return false
    }

    /**
     * Is the given verse entirely within our range. For example if this =
     * "Gen 1:1-31" then: <tt>contains(Verse("Gen 1:3")) == true</tt>
     * <tt>contains(Verse("Gen 2:1")) == false</tt>
     *
     * @param that
     * The Verse to compare to
     * @return true if we contain it.
     */
    fun contains(that: Verse): Boolean {
        return v11n.distance(start, that) >= 0 && v11n.distance(that, end) >= 0
    }

    /**
     * Is the given range within our range. For example if this = "Gen 1:1-31"
     * then: <tt>this.contains(Verse("Gen 1:3-10")) == true</tt>
     * <tt>this.contains(Verse("Gen 2:1-1")) == false</tt>
     *
     * @param that
     * The Verse to compare to
     * @return true if we contain it.
     */
    fun contains(that: VerseRange): Boolean {
        return v11n.distance(start, that.start) >= 0 && v11n.distance(that.end, end) >= 0
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#contains(org.crosswire.jsword.passage.Key)
     */
    override fun contains(key: Key): Boolean {
        if (key is VerseRange) {
            return contains(key)
        }
        if (key is Verse) {
            return contains(key)
        }
        return false
    }

    val isWholeChapter: Boolean
        /**
         * Does this range represent exactly one chapter, no more or less.
         *
         * @return true if we are exactly one chapter.
         */
        get() = v11n.isSameChapter(start, end) && isWholeChapters

    val isWholeChapters: Boolean
        /**
         * Does this range represent a number of whole chapters
         *
         * @return true if we are a whole number of chapters.
         */
        get() = v11n.isStartOfChapter(start) && v11n.isEndOfChapter(end)

    val isWholeBook: Boolean
        /**
         * Does this range represent exactly one book, no more or less.
         *
         * @return true if we are exactly one book.
         */
        get() = v11n.isSameBook(start, end) && isWholeBooks

    val isWholeBooks: Boolean
        /**
         * Does this range represent a whole number of books.
         *
         * @return true if we are a whole number of books.
         */
        get() = v11n.isStartOfBook(start) && v11n.isEndOfBook(end)

    val isMultipleBooks: Boolean
        /**
         * Does this range occupy more than one book;
         *
         * @return true if we occupy 2 or more books
         */
        get() = start.book != end.book

    /**
     * Create an array of Verses
     *
     * @return The array of verses that this makes up
     */
    fun toVerseArray(): Array<Verse?> {
        val retcode = arrayOfNulls<Verse>(verseCount)
        val ord = start.ordinal
        for (i in 0 until verseCount) {
            retcode[i] = v11n.decodeOrdinal(ord + i)
        }

        return retcode
    }

    /**
     * Enumerate the subranges in this range
     *
     * @param restrict
     * @return a range iterator
     */
//    fun rangeIterator(restrict: RestrictionType?): Iterator<VerseRange> {
//        return VerseRangeIterator(v11n, iterator(), restrict)
//    }

    private fun doGetName(base: Key?): String {
        // Cache these we're going to be using them a lot.
        val startBook = start.book
        val startChapter = start.chapter
        val startVerse = start.verse
        val endBook = end.book
        val endChapter = end.chapter
        val endVerse = end.verse

        // If this is in 2 separate books
        if (startBook != endBook) {
            // This range is exactly a whole book
            if (isWholeBooks) {
                // Just report the name of the book, we don't need to worry
                // about the base since we start at the start of a book,
                // and should have been recently normalized()
                return v11n.getPreferredName(startBook) + RANGE_PREF_DELIM + v11n.getPreferredName(
                    endBook
                )
            }

            // If this range is exactly a whole chapter
            if (isWholeChapters) {
                // Just report book and chapter names
                return (v11n.getPreferredName(startBook) + Verse.VERSE_PREF_DELIM1 + startChapter + RANGE_PREF_DELIM
                        + v11n.getPreferredName(endBook) + Verse.VERSE_PREF_DELIM1 + endChapter)
            }

            if (v11n.isChapterIntro(start)) {
                return v11n.getPreferredName(startBook) + Verse.VERSE_PREF_DELIM1 + startChapter + RANGE_PREF_DELIM + end.getName(
                    base
                )
            }
            if (v11n.isBookIntro(start)) {
                return v11n.getPreferredName(startBook) + RANGE_PREF_DELIM + end.getName(base)
            }
            return start.getName(base) + RANGE_PREF_DELIM + end.getName(base)
        }

        // This range is exactly a whole book
        if (isWholeBook) {
            // Just report the name of the book, we don't need to worry about
            // the
            // base since we start at the start of a book, and should have been
            // recently normalized()
            return v11n.getPreferredName(startBook)
        }

        // If this is 2 separate chapters
        if (startChapter != endChapter) {
            // If this range is a whole number of chapters
            if (isWholeChapters) {
                // Just report the name of the book and the chapters
                return v11n.getPreferredName(startBook) + Verse.VERSE_PREF_DELIM1 + startChapter + RANGE_PREF_DELIM + endChapter
            }

            return start.getName(base) + RANGE_PREF_DELIM + endChapter + Verse.VERSE_PREF_DELIM2 + endVerse
        }

        // If this range is exactly a whole chapter
        if (isWholeChapter) {
            // Just report the name of the book and the chapter
            return v11n.getPreferredName(startBook) + Verse.VERSE_PREF_DELIM1 + startChapter
        }

        // If this is 2 separate verses
        if (startVerse != endVerse) {
            return start.getName(base) + RANGE_PREF_DELIM + endVerse
        }

        // The range is a single verse
        return start.getName(base)
    }

    /**
     * Calculate the last verse in this range.
     *
     * @return The last verse in the range
     */
    private fun calcEnd(): Verse? {
        if (verseCount == 1) {
            return start
        }
        return v11n.add(start, verseCount - 1)
    }

    /**
     * Calculate how many verses in this range
     *
     * @return The number of verses. Always&gt;= 1.
     */
    private fun calcVerseCount(): Int {
        return v11n.distance(start, end) + 1
    }

    /**
     * Check to see that everything is ok with the Data
     */
    private fun verifyData() {
//        assert(verseCount == calcVerseCount()) { "start=$start, end=$end, verseCount=$verseCount" }
    }

    /**
     * Write out the object to the given ObjectOutputStream
     *
     * @param out
     * The stream to write our state to
     * @throws IOException
     * If the write fails
     * @serialData Write the ordinal number of this verse
     */
//    private fun writeObject(out: java.io.ObjectOutputStream) {
//        out.defaultWriteObject()
//
//        // Ignore the original name. Is this wise?
//        // I am expecting that people are not that fussed about it and
//        // it could make everything far more verbose
//    }
//
//    /**
//     * Write out the object to the given ObjectOutputStream
//     *
//     * @param in
//     * The stream to read our state from
//     * @throws IOException
//     * If the write fails
//     * @throws ClassNotFoundException
//     * If the read data is incorrect
//     * @serialData Write the ordinal number of this verse
//     */
//    @Throws(java.io.IOException::class, java.lang.ClassNotFoundException::class)
//    private fun readObject(`in`: java.io.ObjectInputStream) {
//        `in`.defaultReadObject()
//
//        end = calcEnd()
//        shaper = NumberShaper()
//
//        verifyData()
//
//        // We are ignoring the originalName and parent.
//    }

    /**
     * Iterate over the Verses in the VerseRange
     */
    private class VerseIterator(range: VerseRange) : MutableIterator<Key> {
        /* (non-Javadoc)
         * @see java.util.Iterator#hasNext()
         */
        override fun hasNext(): Boolean {
            return nextVerse != null
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#next()
         */
        override fun next(): Key {
            if (nextVerse == null) {
                throw NoSuchElementException()
            }
            val currentVerse: Verse = nextVerse!!
            nextVerse = if (++count < total) v11n.next(nextVerse!!) else null
            return currentVerse
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#remove()
         */
        override fun remove() {
            throw UnsupportedOperationException()
        }

        private val v11n: Versification = range.getVersification()
        private var nextVerse: Verse?
        private var count = 0
        private val total: Int

        /**
         * Ctor
         */
        init {
            nextVerse = range.start
            total = range.getCardinality()
        }
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#canHaveChildren()
     */
    override fun canHaveChildren(): Boolean {
        return false
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#getChildCount()
     */
    override fun getChildCount(): Int {
        return 0
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#getCardinality()
     */
    override fun getCardinality(): Int {
        return verseCount
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#isEmpty()
     */
    override fun isEmpty(): Boolean {
        return verseCount == 0
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.crosswire.jsword.passage.Key#iterator()
     */
    override fun iterator(): Iterator<Key> {
        return VerseIterator(this)
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#addAll(org.crosswire.jsword.passage.Key)
     */
    override fun addAll(key: Key) {
        throw UnsupportedOperationException()
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#removeAll(org.crosswire.jsword.passage.Key)
     */
    override fun removeAll(key: Key) {
        throw UnsupportedOperationException()
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#retainAll(org.crosswire.jsword.passage.Key)
     */
    override fun retainAll(key: Key) {
        throw UnsupportedOperationException()
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#clear()
     */
    override fun clear() {
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#get(int)
     */
    override fun get(index: Int): Key? {
        return null
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#indexOf(org.crosswire.jsword.passage.Key)
     */
    override fun indexOf(that: Key): Int {
        return -1
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#blur(int, org.crosswire.jsword.passage.RestrictionType)
     */
//    fun blur(by: Int, restrict: RestrictionType) {
//        val newRange: VerseRange = restrict.blur(v11n, this, by, by)
//        start = newRange.start
//        end = newRange.end
//        verseCount = newRange.verseCount
//    }

    /**
     * The Versification with which this range is defined.
     */
    private val v11n: Versification

    /**
     * Fetch the first verse in this range.
     *
     * @return The first verse in the range
     */
    /**
     * The start of the range
     */
    val start: Verse

    /**
     * The number of verses in the range
     */
    private var verseCount = 0

    /**
     * Fetch the last verse in this range.
     *
     * @return The last verse in the range
     */
    /**
     * The last verse. Not actually needed, since it can be computed.
     */
    val end: Verse

    /**
     * Allow the conversion to and from other number representations.
     */
//    @Transient
//    private var shaper: NumberShaper

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#getParent()
     */
    /**
     * Set a parent Key. This allows us to follow the Key interface more
     * closely, although the concept of a parent for a verse is fairly alien.
     *
     * @param parent
     * The parent Key for this verse
     */
    /**
     * The parent key.
     */
    @Transient
    var parent: Key? = null

    /**
     * The original string for picky users
     */
    @Transient
    private var originalName: String? = null

    companion object {
        /**
         * Create a VerseRange that is the stuff left of VerseRange a when you
         * remove the stuff in VerseRange b.
         *
         * @param a
         * Verses at the start or end of b
         * @param b
         * All the verses
         * @return A list of the Verses outstanding
         */
        fun remainder(a: VerseRange, b: VerseRange): Array<VerseRange> {
            var rstart: VerseRange? = null
            var rend: VerseRange? = null

            val v11n: Versification = a.getVersification()

            // If a starts before b get the Range of the prequel
            if (v11n.distance(a.start, b.start) > 0) {
                rstart = VerseRange(v11n, a.start, v11n.subtract(b.end, 1))
            }

            // If a ends after b get the Range of the sequel
            if (v11n.distance(a.end, b.end) < 0) {
                rend = VerseRange(v11n, v11n.add(b.end, 1), a.end)
            }

            if (rstart == null) {
                if (rend == null) {
                    return arrayOf()
                }
                return arrayOf(
                    rend
                )
            }

            if (rend == null) {
                return arrayOf(
                    rstart
                )
            }
            return arrayOf(
                rstart, rend
            )
        }

        /**
         * Create a VerseRange that is the stuff in VerseRange a that is also
         * in VerseRange b.
         *
         * @param a
         * The verses that you might want
         * @param b
         * The verses that you definitely don't
         * @return A list of the Verses outstanding
         */
        fun intersection(a: VerseRange, b: VerseRange): VerseRange? {
            val v11n = a.getVersification()
            val newStart: Verse = v11n.max(a.start, b.start)
            val newEnd: Verse = v11n.min(a.end, b.end)

            if (v11n.distance(newStart, newEnd) >= 0) {
                return VerseRange(a.getVersification(), newStart, newEnd)
            }

            return null
        }

        /**
         * What characters can we use to separate the 2 parts to a VerseRanges
         */
        const val RANGE_OSIS_DELIM: Char = '-'

        /**
         * What characters should we use to separate VerseRange parts on output
         */
        const val RANGE_PREF_DELIM: Char = RANGE_OSIS_DELIM

        /**
         * Serialization ID
         */
        const val serialVersionUID: Long = 8307795549869653580L
    }
}
