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
package org.crosswire.jsword.book.sword

import org.crosswire.jsword.book.sword.state.OpenFileState
import org.crosswire.jsword.passage.Key

/**
 * Indicates that there is a stateful backend
 *
 *
 * @param <T> The type of the OpenFileState that this class extends.
 * @see gnu.lgpl.License The GNU Lesser General Public License for details.
 *
 * @author DM Smith
</T> */
interface StatefulFileBackedBackend<T : OpenFileState> {
    /**
     * Initialises the state required to read from files, specific to each
     * different backend
     *
     * @return the state that has been initialised
     * @throws BookException
     */
    fun initState(): T

    /**
     *
     * @param state
     * the state object containing all the open random access files
     * @param key
     * the verse that is sought
     * @return the raw text
     * @throws BookException
     * @throws IOException
     * something went wrong when reading the verse
     */
    fun readRawContent(state: T, key: Key): String
}
