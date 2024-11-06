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
package org.crosswire.jsword.book.sword

import org.crosswire.jsword.book.BookMetaData
import org.crosswire.jsword.book.sword.state.OpenFileState
import org.crosswire.jsword.passage.Key
import org.crosswire.jsword.passage.KeyText

/**
 * Uniform representation of all Backends.
 *
 * @param <T> The type of the OpenFileState that this class extends.
 */
interface Backend<T : OpenFileState> {

    fun readToOsis(key: Key): List<KeyText>

    /**
     * @return Returns the Sword BookMetaData.
     */
    fun getBookMetaData(): BookMetaData

    /**
     * Get the text as it is found in the Book for the given key
     *
     * @param key the key for which the raw text is desired.
     * @return the text from the module
     * @throws BookException
     */
    fun getRawText(key: Key): String
}
