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
package org.crosswire.jsword.book.basic

import org.crosswire.jsword.book.BookMetaData
import org.crosswire.jsword.book.KeyType

/**
 * An implementation of the Property Change methods from BookMetaData.
 *
 * @see gnu.lgpl.License The GNU Lesser General Public License for details.
 *
 * @author Joe Walker
 */
/**
 * @author DM Smith
 * @see gnu.lgpl.License The GNU Lesser General Public License for details.<br></br>
 * The copyright to this program is held by its authors.
 */
abstract class AbstractBookMetaData : BookMetaData {
    override fun getKeyType(): KeyType {
        return KeyType.VERSE
    }

//    val driverName: String?
//        /* (non-Javadoc)
//     * @see org.crosswire.jsword.book.BookMetaData#getDriverName()
//     */
//        get() {
//            if (driver == null) {
//                return null
//            }
//
//            return driver.getDriverName()
//        }
//
//    /* (non-Javadoc)
//     * @see org.crosswire.jsword.book.BookMetaData#hasFeature(org.crosswire.jsword.book.FeatureType)
//     */
//    fun hasFeature(feature: FeatureType?): Boolean {
//        return false
//    }
//
//    val osisID: String
//        /* (non-Javadoc)
//     * @see org.crosswire.jsword.book.BookMetaData#getOsisID()
//     */
//        get() = (getBookCategory().getName() + '.').toString() + initials
//
//    val isSupported: Boolean
//        /* (non-Javadoc)
//     * @see org.crosswire.jsword.book.BookMetaData#isSupported()
//     */
//        get() = true
//
//    val isEnciphered: Boolean
//        /* (non-Javadoc)
//     * @see org.crosswire.jsword.book.BookMetaData#isEnciphered()
//     */
//        get() = false
//
//    val isLocked: Boolean
//        /* (non-Javadoc)
//     * @see org.crosswire.jsword.book.BookMetaData#isLocked()
//     */
//        get() = false
//
//    /* (non-Javadoc)
//     * @see org.crosswire.jsword.book.BookMetaData#unlock(java.lang.String)
//     */
//    override fun unlock(unlockKey: String?): Boolean {
//        return false
//    }
//
//    val unlockKey: String?
//        /* (non-Javadoc)
//     * @see org.crosswire.jsword.book.BookMetaData#getUnlockKey()
//     */
//        get() = null
//
//    val isQuestionable: Boolean
//        /* (non-Javadoc)
//     * @see org.crosswire.jsword.book.BookMetaData#isQuestionable()
//     */
//        get() = false
//
//    /* (non-Javadoc)
//     * @see org.crosswire.jsword.book.BookMetaData#reload()
//     */
//    @Throws(BookException::class)
//    override fun reload() {
//        // over ride this if partial loads are allowed
//    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.book.BookMetaData#putProperty(java.lang.String, java.lang.String)
     */
    override fun putProperty(key: String?, value: String?) {
        putProperty(key, value, false)
    }

//    /* (non-Javadoc)
//     * @see org.crosswire.jsword.book.BookMetaData#toOSIS()
//     */
//    fun toOSIS(): Document {
//        throw java.lang.UnsupportedOperationException("If you want to use this, implement it.")
//    }
//
//    override fun equals(obj: Any?): Boolean {
//        // Since this can not be null
//        if (obj == null) {
//            return false
//        }
//
//        // We might consider checking for equality against all BookMetaDatas?
//        // However currently we don't.
//
//        // Check that that is the same as this
//        // Don't use instanceof since that breaks inheritance
//        if (obj.javaClass != this.javaClass) {
//            return false
//        }
//
//        // The real bit ...
//        val that: BookMetaData = obj as BookMetaData
//
//        return getBookCategory().equals(that.getBookCategory()) && name == that.name && initials == that.initials
//    }
//
//    override fun hashCode(): Int {
//        return name.hashCode()
//    }
//
//    /*
//     * The sort order should be based on initials rather than name because name often begins with general words like 'The ...'
//     * (non-Javadoc)
//     * @see java.lang.Comparable#compareTo(java.lang.Object)
//     */
//    override fun compareTo(obj: BookMetaData): Int {
//        var result: Int = this.getBookCategory().compareTo(obj.getBookCategory())
//        if (result == 0) {
//            result = this.abbreviation.compareTo(obj.abbreviation)
//        }
//        if (result == 0) {
//            result = this.initials.compareTo(obj.initials)
//        }
//        if (result == 0) {
//            result = this.name.compareTo(obj.name)
//        }
//        return result
//    }
//
//    override fun toString(): String {
//        val internal: String = initials
//        val abbreviation: String = abbreviation
//        if (internal == abbreviation) {
//            return internal
//        }
//        val buf: java.lang.StringBuffer = java.lang.StringBuffer(internal)
//        buf.append('(')
//        buf.append(abbreviation)
//        buf.append(')')
//        return buf.toString()
//    }
//
//    /* (non-Javadoc)
//     * @see org.crosswire.jsword.book.BookMetaData#getDriver()
//     */
//    /**
//     * @param driver The driver to set.
//     */
//    var driver: BookDriver? = null
//        /* (non-Javadoc)
//          * @see org.crosswire.jsword.book.BookMetaData#getIndexStatus()
//          */
//        get() = indexStatus
//        /* (non-Javadoc)
//              * @see org.crosswire.jsword.book.BookMetaData#setIndexStatus(org.crosswire.jsword.index.IndexStatus)
//              */  set(newValue) {
//            indexStatus = newValue
//        }
//    private var indexStatus: IndexStatus = IndexStatus.UNDONE
//
//    /* (non-Javadoc)
//     * @see org.crosswire.jsword.book.BookMetaData#getLanguage()
//     *//* (non-Javadoc)
//     * @see org.crosswire.jsword.book.BookMetaData#setLanguage(org.crosswire.common.util.Language)
//     */
//    var language: Language? = null
//

    override lateinit var  library: String

    lateinit var location: String
}
