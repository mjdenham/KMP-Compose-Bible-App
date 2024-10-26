package org.crosswire.jsword.book.sword

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

object SwordBookPath {
    var swordBookPath: Path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve("sword_book".toPath())
}