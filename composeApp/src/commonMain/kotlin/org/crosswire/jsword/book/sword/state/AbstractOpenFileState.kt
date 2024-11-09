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
 * © CrossWire Bible Society, 2013 - 2016
 *
 */
package org.crosswire.jsword.book.sword.state

import kotlinx.datetime.Clock
import org.crosswire.jsword.book.BookMetaData

abstract class AbstractOpenFileState(override val bookMetaData: BookMetaData) : OpenFileState {
    /**
     * Allows us to decide whether to release the resources or continue using them
     */
    override fun close() {
        OpenFileStateManager.release(this)
    }

    /**
     * The time of last access, used for LRU expiration of state.
     */
    override var lastAccess: Long = Clock.System.now().toEpochMilliseconds()
}
