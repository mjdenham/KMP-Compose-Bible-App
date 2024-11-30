package com.martin.bibleapp.domain.osisconverter

/**
 * Determine whether text should be written based on the osis tag hierarchy.
 */
class WriteState {
    val writableStack = ArrayDeque<Boolean>()

    fun canWrite(): Boolean {
        return writableStack.isNotEmpty() && writableStack.last()
    }

    fun openTag(name: String, canonical: String?) {
        val writeTagContents = name in listOf("w", "container", "list", "item") || canonical.toBoolean()
        writableStack.addLast(
            writeTagContents
        )
    }

    fun closeTag() {
        writableStack.removeLast()
    }
}