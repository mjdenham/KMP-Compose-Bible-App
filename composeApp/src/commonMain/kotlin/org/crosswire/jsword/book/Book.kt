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

import org.crosswire.jsword.passage.Key
import org.crosswire.jsword.passage.KeyText

/**
 * Book is the most basic store of textual data - It can retrieve data either as
 * an XML document or as plain text - It uses Keys to refer to parts of itself,
 * and can search for words (returning Keys).
 *
 * @see gnu.lgpl.License The GNU Lesser General Public License for details.
 *
 * @author Joe Walker
 */
interface Book /* : Activatable, Comparable<Book?> */ {
    fun readToOsis(key: Key): List<KeyText>

//    /**
//     * Set the meta-information for this book.
//     *
//     * @param bmd the BookMetaData that describes this book.
//     */
//    var bookMetaData: BookMetaData?

    /**
     * Returns the raw text that getData(Key key) builds into OSIS.
     *
     * @param key
     * The item to locate
     * @return The found Book data
     * @throws BookException
     * If anything goes wrong with this method
     */
    fun getRawText(key: Key): String

//    /**
//     * The name of the book, for example "King James Version" or
//     * "Bible in Basic English" or "Greek". In general it should be possible to
//     * deduce the initials from the name by removing all the non-capital
//     * letters. Although this is only a generalization. This method should not
//     * return null or a blank string.
//     *
//     * @return The name of this book
//     */
//    val name: String?
//
//    /**
//     * What category of content is this, a Bible or a reference work like a
//     * Dictionary or Commentary.
//     *
//     * @return The category of book
//     */
//    val bookCategory: BookCategory?
//
//    /**
//     * Accessor for the driver that runs this Book. Note this method should only
//     * be used to delete() Books. Everything else you should want to do to a
//     * Book should be available in other ways.
//     *
//     * @return the book's driver
//     */
//    val driver: BookDriver?
//
//    /**
//     * The language of the book.
//     *
//     * @return the common name for the language
//     */
//    val language: Language?
//
//    /**
//     * The abbreviation of this book - how people familiar with this book will know
//     * it, for example "NIV", "KJV".
//     *
//     * @return The book's initials
//     */
//    val abbreviation: String?
//
//    /**
//     * The internal name of this book.
//     *
//     * @return The book's internal name
//     */
//    val initials: String?
//
//    /**
//     * Calculated field: Get an OSIS identifier for the OsisText.setOsisIDWork()
//     * and the Work.setOsisWork() methods. The response will generally be of the
//     * form [Bible][Dict..].getInitials
//     *
//     * @return The OSIS id of this book
//     */
//    val osisID: String?
//
//    /**
//     * Indicate whether this book is supported by JSword. Since the expectation
//     * is that all books are supported, abstract implementations should return
//     * true and let specific implementations return false if they cannot support
//     * the book.
//     *
//     * @return true if the book is supported
//     */
//    val isSupported: Boolean
//
//    /**
//     * Indicate whether this book is enciphered. Since the expectation is that
//     * most books are unenciphered, abstract implementations should return false
//     * and let specific implementations return true otherwise.
//     *
//     * @return true if the book is enciphered
//     */
//    val isEnciphered: Boolean
//
//    /**
//     * Indicate whether this book is enciphered and without a key. Since the
//     * expectation is that most books are not encrypted, abstract implementations
//     * should return false and let specific implementations return true
//     * otherwise.
//     *
//     * @return true if the book is locked
//     */
//    val isLocked: Boolean
//
//    /**
//     * Calculated field: The name of the name, which could be helpful to
//     * distinguish similar Books available through 2 BookDrivers.
//     *
//     * @return The driver name
//     */
//    val driverName: String?
//
//    /**
//     * Return the orientation of the script of the Book. If a book contains more
//     * than one script, it refers to the dominate script of the book. This will
//     * be used to present Arabic and Hebrew in their proper orientation. Note:
//     * some languages have multiple scripts which don't have the same
//     * directionality.
//     *
//     * @return true if the orientation for the dominate script is LeftToRight.
//     */
//    val isLeftToRight: Boolean
//
//    /**
//     * Return whether the feature is supported by the book.
//     *
//     * @param feature the type of the Feature to check
//     * @return whether the feature is supported
//     */
//    fun hasFeature(feature: FeatureType?): Boolean
//
//    /**
//     * Get a list of all the properties available to do with this Book. The
//     * returned Properties will be read-only so any attempts to alter it will
//     * fail.
//     *
//     * @return the read-only properties for this book.
//     */
//    val propertyKeys: Set<String?>?
//
//    /**
//     * Retrieve a single property for this book.
//     *
//     * @param key
//     * the key of the property.
//     * @return the value of the property
//     */
//    fun getProperty(key: String?): String?
//
//    /**
//     * Set a property for this book.
//     *
//     * @param key
//     * the key of the property.
//     * @param value
//     * the value of the property
//     */
//    fun putProperty(key: String?, value: String?)
//
//    /**
//     * Saves an entry to a particular configuration file.
//     *
//     * @param key the entry that we are saving
//     * @param value the value of the entry
//     * @param forFrontend when `true` save to front end storage, else in shared storage
//     */
//    fun putProperty(key: String?, value: String?, forFrontend: Boolean)
//
}
