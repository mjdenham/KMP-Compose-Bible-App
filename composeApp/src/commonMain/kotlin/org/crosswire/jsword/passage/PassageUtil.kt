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

/**
 * A Utility class containing various static methods.
 *
 * @see gnu.lgpl.License The GNU Lesser General Public License for details.
 *
 * @author Joe Walker
 */
object PassageUtil {
    /**
     * By default do we remember the original string used to configure us?
     *
     * @return false getDefaultPersistentNaming() is always false
     */
    val defaultPersistentNaming: Boolean = false

    /**
     * Do we remember the original string used to configure us?
     *
     * @param persistentNaming
     * True to keep the old string False (default) to generate a new
     * better one
     */
    var isPersistentNaming: Boolean = defaultPersistentNaming
}
