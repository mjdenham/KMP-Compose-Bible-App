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

    override fun putProperty(key: String?, value: String?) {
        putProperty(key, value, false)
    }

    override lateinit var  library: String

    // library/datapath = location of the datafiles e.g. mnt/sdcard/Android/data/packagename/files/modules/texts/ztext/bsb/
    lateinit var location: String
}
