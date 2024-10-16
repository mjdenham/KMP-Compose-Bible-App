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

import org.crosswire.common.util.ItemIterator
import org.crosswire.jsword.versification.BibleBook
import org.crosswire.jsword.versification.BibleNames
import org.crosswire.jsword.versification.Versification
import org.crosswire.jsword.versification.system.Versifications
import kotlin.jvm.Transient

/**
 * A Verse is a pointer to a single verse. Externally its unique identifier is
 * a String of the form "Gen 1:1" Internally we use
 * `( v11n, book, chapter, verse )`
 *
 *
 *
 * A Verse is designed to be immutable. This is a necessary from a collections
 * point of view. A Verse should always be valid, although some versions may not
 * return any text for verses that they consider to be untranslated in some
 * way.
 *
 *
 * @author Joe Walker
 * @author DM Smith
 */
class Verse : VerseKey<Verse> {
    /**
     * Create a Verse from book, chapter and verse numbers, throwing up if the
     * specified Verse does not exist.
     *
     * @param v11n
     * The versification to which this verse belongs
     * @param book
     * The book number (Genesis = 1)
     * @param chapter
     * The chapter number
     * @param verse
     * The verse number
     */
    constructor(v11n: Versification, book: BibleBook, chapter: Int, verse: Int) : this(
        v11n,
        book,
        chapter,
        verse,
        null
    )

    /**
     * Create a Verse from book, chapter and verse numbers, throwing up if the
     * specified Verse does not exist.
     *
     * @param v11n
     * The versification to which this verse belongs
     * @param book
     * The book number (Genesis = 1)
     * @param chapter
     * The chapter number
     * @param verse
     * The verse number
     * @param subIdentifier
     * The optional sub identifier
     */
    constructor(
        v11n: Versification,
        book: BibleBook,
        chapter: Int,
        verse: Int,
        subIdentifier: String?
    ) {
        this.v11n = v11n
        this.book = book
        this.chapter = chapter
        this.verse = verse
        this.subIdentifier = subIdentifier
        this.ordinal = v11n.getOrdinal(this)
    }

    /**
     * Create a Verse from book, chapter and verse numbers, patching up if the
     * specified verse does not exist.
     *
     *
     * The actual value of the boolean is ignored. However for future proofing
     * you should only use 'true'. Do not use patch_up=false, use
     * `Verse(int, int, int)` This so that we can declare this
     * constructor to not throw an exception. Is there a better way of doing
     * this?
     *
     * @param v11n
     * The versification to which this verse belongs
     * @param book
     * The book number (Genesis = 1)
     * @param chapter
     * The chapter number
     * @param verse
     * The verse number
     * @param patchUp
     * True to trigger reference fixing
     */
    constructor(
        v11n: Versification,
        book: BibleBook?,
        chapter: Int,
        verse: Int,
        patchUp: Boolean
    ) {
        if (!patchUp) {
//            throw new IllegalArgumentException(JSOtherMsg.lookupText("Use patchUp=true."));
        }

        this.v11n = v11n
        val patched: Verse = this.v11n.patch(book, chapter, verse)
        this.book = patched.book
        this.chapter = patched.chapter
        this.verse = patched.verse
        this.ordinal = patched.ordinal
    }

    /**
     * Set a Verse using a verse ordinal number - WARNING Do not use this method
     * unless you really know the dangers of doing so. Ordinals are not always
     * going to be the same. So you should use Versification, Book, Chapter and Verse
     * in preference to an int ordinal whenever possible. Ordinal numbers are 1
     * based and not 0 based.
     *
     * @param v11n
     * The versification to which this verse belongs
     * @param ordinal
     * The verse id
     */
    constructor(v11n: Versification, ordinal: Int) {
        val decoded: Verse = v11n.decodeOrdinal(ordinal)
        this.v11n = v11n
        this.book = decoded.book
        this.chapter = decoded.chapter
        this.verse = decoded.verse
        this.ordinal = decoded.ordinal
    }

    override fun isWhole(): Boolean {
        return subIdentifier == null || subIdentifier!!.length == 0
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.VerseKey#getWhole()
     */
    override fun getWhole(): Verse {
        if (isWhole()) {
            return this
        }
        return Verse(v11n, book, chapter, verse)
    }

    override fun toString(): String {
        return getName()
    }

    override fun getName(): String {
        return getName(null)
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#getName(org.crosswire.jsword.passage.Key)
     */
    override fun getName(base: Key?): String {
        if (base != null && base !is Verse) {
            return getName()
        }

        val verseName = doGetName(base as Verse?)

        // Only shape it if it can be unshaped.
//        if (shaper.canUnshape()) {
//            return shaper.shape(verseName);
//        }
        return verseName
    }

    override fun getRootName(): String? {
        return BibleNames.instance().getShortName(book)
    }

    override fun getOsisRef(): String? {
        return getOsisID()
    }

    override fun getOsisID(): String {
        val buf: StringBuilder = getVerseIdentifier()
        if (subIdentifier != null && subIdentifier!!.length > 0) {
            buf.append(VERSE_OSIS_SUB_PREFIX)
            buf.append(subIdentifier)
        }
        return buf.toString()
    }

    fun getOsisIDNoSubIdentifier(): String {
        return getVerseIdentifier().toString()
    }

    /**
     * Gets the common name of the verse, excluding any !abc sub-identifier
     * @return the verse OSIS-ID, excluding the sub-identifier
     */
    private fun getVerseIdentifier(): StringBuilder {
        val buf = StringBuilder()
        buf.append(book.oSIS)
        buf.append(VERSE_OSIS_DELIM)
        buf.append(chapter)
        buf.append(VERSE_OSIS_DELIM)
        buf.append(verse)
        return buf
    }

    override fun clone(): Verse {
        //TODO
//        var copy: Verse? = null
//        try {
//            copy = super.clone() as Verse?
//            copy!!.v11n = this.v11n
//            copy.book = this.book
//            copy.chapter = this.chapter
//            copy.verse = this.verse
//            copy.ordinal = this.ordinal
//            copy.subIdentifier = this.subIdentifier
//        } catch (e: java.lang.CloneNotSupportedException) {
//            assert(false) { e }
//        }
//
//        return copy
        return this
    }

    override fun equals(obj: Any?): Boolean {
        // Since this can not be null
        if (obj !is Verse) {
            return false
        }

        val that = obj

        // The real tests
        return this.ordinal == that.ordinal && (this.v11n == that.v11n) && bothNullOrEqual(
            this.subIdentifier, that.subIdentifier
        )
    }

    override fun hashCode(): Int {
        var result = 31 + ordinal
        result = 31 * result + (if ((v11n == null)) 0 else v11n.hashCode())
        return 31 * result + (if ((subIdentifier == null)) 0 else subIdentifier.hashCode())
    }

    /* (non-Javadoc)
     * @see Comparable#compareTo(Object)
     */
    override fun compareTo(obj: Key): Int {
        return this.ordinal - (obj as Verse).ordinal
    }

    override fun getVersification(): Versification {
        return v11n!!
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Passage#reversify(org.crosswire.jsword.versification.Versification)
     */
    fun reversify(newVersification: Versification): Verse? {
        if (v11n == newVersification) {
            return this
        }

        try {
            //check the v11n supports this key, otherwise this leads to all sorts of issues
//            if (newVersification.validate(book, chapter, verse, true)) {
//                return new Verse(newVersification, book, chapter, verse);
//            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            // will never happen
//            log.error("Contract for validate was changed to thrown an exception when silent mode is true", ex);
        }
        return null
    }

    /**
     * Create an array of Verses
     *
     * @return The array of verses that this makes up
     */
    fun toVerseArray(): Array<Verse> {
        return arrayOf(
            this
        )
    }

    val parent: Key?
        /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#getParent()
     */
        get() = null

    /**
     * Compute the verse representation given the context.
     *
     * @param verseBase
     * the context or null if there is none
     * @return the verse representation
     */
    private fun doGetName(verseBase: Verse?): String {
        val buf: StringBuilder = StringBuilder()
        // To cope with thing like Jude 2...
        if (book.isShortBook) {
            if (verseBase == null || verseBase.book !== book) {
                buf.append(BibleNames.instance().getPreferredName(book))
                buf.append(VERSE_PREF_DELIM1)
                buf.append(verse)
                return buf.toString()
            }

            return verse.toString()
        }

        if (verseBase == null || verseBase.book !== book) {
            buf.append(BibleNames.instance().getPreferredName(book))
            buf.append(VERSE_PREF_DELIM1)
            buf.append(chapter)
            buf.append(VERSE_PREF_DELIM2)
            buf.append(verse)
            return buf.toString()
        }

        if (verseBase.chapter != chapter) {
            buf.append(chapter)
            buf.append(VERSE_PREF_DELIM2)
            buf.append(verse)
            return buf.toString()
        }

        return verse.toString()
    }

//    /**
//     * Write out the object to the given ObjectOutputStream
//     *
//     * @param out
//     * The stream to write our state to
//     * @throws IOException
//     * if the read fails
//     * @serialData Write the ordinal number of this verse
//     */
//    private fun writeObject(out: ObjectOutputStream) {
//        out.defaultWriteObject()
//        out.writeUTF(v11n.name)
//    }
//
//    /**
//     * Write out the object to the given ObjectOutputStream
//     *
//     * @param in
//     * The stream to read our state from
//     * @throws IOException
//     * if the read fails
//     * @throws ClassNotFoundException
//     * If the read data is incorrect
//     * @serialData Write the ordinal number of this verse
//     */
//    private fun readObject(`in`: java.io.ObjectInputStream) {
//        `in`.defaultReadObject()
//        val v11nName: String = `in`.readUTF()
//        v11n = Versifications.instance().getVersification(v11nName)
//        val decoded: Verse = v11n.decodeOrdinal(ordinal)
//
//        this.book = decoded.book
//        this.chapter = decoded.chapter
//        this.verse = decoded.verse
//    }

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
        return 1
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#isEmpty()
     */
    override fun isEmpty(): Boolean {
        return false
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#contains(org.crosswire.jsword.passage.Key)
     */
    override fun contains(key: Key): Boolean {
        return this == key
    }

    /* (non-Javadoc)
     * @see Iterable#iterator()
     */
    override fun iterator(): Iterator<Key> {
        return ItemIterator<Key>(this);
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
        // do nothing
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#get(int)
     */
    override fun get(index: Int): Key? {
        if (index == 0) {
            return this
        }
        return null
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#indexOf(org.crosswire.jsword.passage.Key)
     */
    override fun indexOf(that: Key): Int {
        if (this == that) {
            return 0
        }
        return -1
    }

    /**
     * Allow the conversion to and from other number representations.
     */
    //    private static NumberShaper shaper = new NumberShaper();
    /**
     * The versification for this verse.
     */
    @Transient
    private var v11n: Versification

    /**
     * Return the ordinal value of the verse in its versification.
     *
     * @return The verse number
     */
    /**
     * The ordinal value for this verse within its versification.
     */
    var ordinal: Int
        private set

    /**
     * The book of the Bible.
     */
    @Transient
    var book: BibleBook
        private set

    /**
     * Return the chapter that we refer to
     *
     * @return The chapter number
     */
    /**
     * The chapter number
     */
    @Transient
    var chapter: Int
        private set

    /**
     * Return the verse that we refer to
     *
     * @return The verse number
     */
    /**
     * The verse number
     */
    @Transient
    var verse: Int
        private set

    /**
     * Return the sub identifier if any
     * @return The optional OSIS sub identifier
     */
    /**
     * The OSIS Sub-identifier if present.
     * This should be a string that allows for the likes of:
     * a.xy.asdf.qr
     */
    var subIdentifier: String? = null
        private set

    companion object {
        /**
         * Determine whether two objects are equal, allowing nulls
         * @param x
         * @param y
         * @return true if both are null or the two are equal
         */
        fun bothNullOrEqual(x: Any?, y: Any?): Boolean {
            return x === y || (x != null && x == y)
        }

        /**
         * This is simply a convenience function to wrap Integer.parseInt() and give
         * us a reasonable exception on failure. It is called by VerseRange hence
         * protected, however I would prefer private
         *
         * @param text
         * The string to be parsed
         * @return The correctly parsed chapter or verse
         */
        protected fun parseInt(text: String): Int {
            try {
                return text.toInt() //shaper.unshape(text));
            } catch (ex: NumberFormatException) {
                // TRANSLATOR: The chapter or verse number is actually not a number, but something else.
                // {0} is a placeholder for what the user supplied.
                ex.printStackTrace()
                throw NoSuchVerseException("Cannot understand {0} as a chapter or verse.")
            }
        }

        /* (non-Javadoc)
     * @see org.crosswire.jsword.passage.Key#blur(int, org.crosswire.jsword.passage.RestrictionType)
     */
        //    public void blur(int by, RestrictionType restrict) {
        //        throw new UnsupportedOperationException();
        //    }
        /**
         * What characters should we use to separate parts of an OSIS verse
         * reference
         */
        const val VERSE_OSIS_DELIM: Char = '.'

        /**
         * What characters should we use to start an OSIS sub identifier
         */
        const val VERSE_OSIS_SUB_PREFIX: Char = '!'

        /**
         * What characters should we use to separate the book from the chapter
         */
        const val VERSE_PREF_DELIM1: Char = ' '

        /**
         * What characters should we use to separate the chapter from the verse
         */
        const val VERSE_PREF_DELIM2: Char = ':'

        /**
         * The default verse
         */
        val DEFAULT: Verse =
            Verse(Versifications.instance().getVersification("KJV"), BibleBook.GEN, 1, 1)

        //    private static final Logger log = LoggerFactory.getLogger(Verse.class);
        /**
         * To make serialization work across new versions
         */
        private const val serialVersionUID = -4033921076023185171L
    }
}
