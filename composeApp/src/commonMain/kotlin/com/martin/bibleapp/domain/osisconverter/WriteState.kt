package com.martin.bibleapp.domain.osisconverter

/**
 * Determine whether text should be written based on the osis tag hierarchy.
 */
class WriteState {
    val writableStack = ArrayDeque<Boolean>()

    fun canWrite(): Boolean {
        return writableStack.isNotEmpty() && writableStack.last()
    }

    fun openTag(name: String) {
        writableStack.addLast(
            name in listOf("w", "container", "list", "item")
        )
    }

    fun closeTag() {
        writableStack.removeLast()
    }
}