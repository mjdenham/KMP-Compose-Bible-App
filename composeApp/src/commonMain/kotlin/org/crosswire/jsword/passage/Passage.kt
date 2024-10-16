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

import org.crosswire.jsword.passage.NoSuchVerseException

/**
 * A Passage is a specialized Collection of Verses. The additions are:
 *
 *  * List blurring
 *  * Range Counting and iteration (in addition to Verse counting etc)
 *  * List change notification, so you can register to update yourself, and
 * this goes hand in hand with a added thread-safe contract.
 *  * getName() to be more VerseBase like.
 *  * Human readable serialization. So we can read and write to and from OLB
 * style Passage files.
 *
 *
 *
 *
 * Passage no longer extends the Collection interface to avoid J2SE 1.1/1.2
 * portability problems, and because many of the things that a Passage does rely
 * on consecutive Verses which are an alien concept to Collections. So users
 * would have to use the Passage interface anyway.
 *
 *
 *
 * Other arguments for and against.
 *
 *  * The generic version will postpone some type errors to runtime. Is this a
 * huge problem? Are there many syntax errors that would be lost? Probably not.
 *  * The specific version would stop enhancements like add("Gen 1:1"); (But
 * this is just syntactical sugar anyway).
 *  * The specific version allows functionality by is-a as well as has-a. But a
 * Passage is fundamentally different so this is not that much use.
 *  * At the end of the day I expect people to use getName() instead of
 * toString() and blur(), both of which are Passage things not Collection
 * things. So the general use of these classes is via a Passage interface not a
 * Collections one.
 *  * Note that the implementations of Passage could not adhere strictly to the
 * Collections interface in returning false from add(), remove() etc, to specify
 * if the Collection was changed. Given ranges and the like this can get very
 * time consuming and complex.
 *
 *
 *
 *
 * The upshot of all this is that I am removing the Collections interface from
 * Passage.
 *
 *
 *
 * I considered giving Passages names to allow for a CLI that could use named
 * RangedPassages, however that is perhaps better left to another class.
 *
 * @see gnu.lgpl.License The GNU Lesser General Public License for details.
 *
 * @author Joe Walker
 */
interface Passage : VerseKey<Passage?> {
    /**
     * A summary of the verses in this Passage For example
     * "10 verses in 4 books"
     *
     * @return a String containing an overview of the verses
     */
    val overview: String?

    /**
     * Returns the number of verses in this collection. Like Collection.size()
     * This does not mean the Passage needs to use Verses, just that it
     * understands the concept.
     *
     * @return the number of Verses in this collection
     * @see Verse
     */
    fun countVerses(): Int

    /**
     * Determine whether there are two or more ranges.
     *
     * @param restrict
     * Do we break ranges at chapter/book boundaries
     * @return whether there are two or more ranges
     * @see VerseRange
     */
    fun hasRanges(restrict: RestrictionType): Boolean

    /**
     * Like countVerses() that counts VerseRanges instead of Verses Returns the
     * number of fragments in this collection. This does not mean the Passage
     * needs to use VerseRanges, just that it understands the concept.
     *
     * @param restrict
     * Do we break ranges at chapter/book boundaries
     * @return the number of VerseRanges in this collection
     * @see VerseRange
     */
    fun countRanges(restrict: RestrictionType): Int

    /**
     * Ensures that there are a maximum of `count` Verses in this
     * Passage. If there were more than `count` Verses then a new
     * Passage is created containing the Verses from `count`+1
     * onwards. If there was not greater than `count` in the Passage,
     * then the passage remains unchanged, and null is returned.
     *
     * @param count
     * The maximum number of Verses to allow in this collection
     * @return A new Passage containing the remaining verses or null
     * @see Verse
     */
    fun trimVerses(count: Int): Passage?

    /**
     * Ensures that there are a maximum of `count` VerseRanges in
     * this Passage. If there were more than `count` VerseRanges then
     * a new Passage is created containing the VerseRanges from
     * `count`+1 onwards. If there was not greater than
     * `count` in the Passage, then the passage remains unchanged,
     * and null is returned.
     *
     * @param count
     * The maximum number of VerseRanges to allow in this collection
     * @param restrict
     * Do we break ranges at chapter/book boundaries
     * @return A new Passage containing the remaining verses or null
     * @see VerseRange
     */
    fun trimRanges(count: Int, restrict: RestrictionType?): Passage?

    /**
     * How many books are there in this Passage
     *
     * @return The number of distinct books
     */
    fun booksInPassage(): Int

    /**
     * Get a specific Verse from this collection
     *
     * @param offset
     * The verse offset (legal values are 0 to countVerses()-1)
     * @return The Verse
     * @throws ArrayIndexOutOfBoundsException
     * If the offset is out of range
     */
    fun getVerseAt(offset: Int): Verse?

    /**
     * Get a specific VerseRange from this collection
     *
     * @param offset
     * The verse range offset (legal values are 0 to countRanges()-1)
     * @param restrict
     * Do we break ranges at chapter/book boundaries
     * @return The Verse Range
     * If the offset is out of range
     */
    fun getRangeAt(offset: Int, restrict: RestrictionType): VerseRange?

    /**
     * Like iterator() that iterates over VerseRanges instead of Verses.
     * Exactly the same data will be traversed, however using rangeIterator()
     * will usually give fewer iterations (and never more)
     *
     * @param restrict
     * Do we break ranges over chapters
     * @return A list enumerator
     */
    fun rangeIterator(restrict: RestrictionType): Iterator<VerseRange>

    /**
     * Returns true if this collection contains all the specified Verse
     *
     * @param that
     * Verse or VerseRange that may exist in this Passage
     * @return true if this collection contains that
     */
    override fun contains(that: Key): Boolean

    /**
     * Add this Verse/VerseRange to this Passage
     *
     * @param that
     * The Verses to be added from this Passage
     */
    fun add(that: Key?)

    /**
     * Remove this Verse/VerseRange from this Passage
     *
     * @param that
     * The Verses to be removed from this Passage
     */
    fun remove(that: Key?)

    /**
     * Returns true if this Passage contains all of the verses in that Passage
     *
     * @param that
     * Passage to be checked for containment in this collection.
     * @return true if this reference contains all of the Verses in that Passage
     */
    fun containsAll(that: Passage?): Boolean

    /**
     * To be compatible with humans we read/write ourselves to a file that a
     * human can read and even edit. OLB verse.lst integration is a good goal
     * here.
     *
     * @param in
     * The stream to read from
     * @exception IOException
     * If the file/network etc breaks
     * @exception NoSuchVerseException
     * If the file was invalid
     */
//    @Throws(java.io.IOException::class, NoSuchVerseException::class)
//    fun readDescription(`in`: java.io.Reader?)

    /**
     * To be compatible with humans we read/write ourselves to a file that a
     * human can read and even edit. OLB verse.lst integration is a good goal
     * here.
     *
     * @param out
     * The stream to write to
     * @exception IOException
     * If the file/network etc breaks
     */
//    @Throws(java.io.IOException::class)
//    fun writeDescription(out: java.io.Writer?)

    /**
     * For performance reasons we may well want to hint to the Passage that we
     * have done editing it for now and that it is safe to cache certain values
     * to speed up future reads. Any action taken by this method will be undone
     * simply by making a future edit, and the only loss in calling
     * optimizeReads() is a loss of time if you then persist in writing to the
     * Passage.
     */
    fun optimizeReads()

    /**
     * Event Listeners - Add Listener
     *
     * @param li
     * The listener to add
     */
//    fun addPassageListener(li: PassageListener?)

    /**
     * Event Listeners - Remove Listener
     *
     * @param li
     * The listener to remove
     */
//    fun removePassageListener(li: PassageListener?)
}
