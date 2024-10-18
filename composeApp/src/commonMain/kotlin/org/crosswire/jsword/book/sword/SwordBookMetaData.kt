package org.crosswire.jsword.book.sword

import org.crosswire.jsword.book.BookMetaData
import org.crosswire.jsword.book.KeyType

class SwordBookMetaData: BookMetaData {
    override fun getKeyType(): KeyType {
        //TODO
//        val bt: BookType = getBookType() ?: return null
//        return bt.getKeyType()
        return KeyType.VERSE
    }

    /**
     * @return the book type
     */
//    fun getBookType(): BookType {
//        return bookType
//    }


    override val name: String
        get() = TODO("Not yet implemented")
    override val bookCharset: String?
        get() = TODO("Not yet implemented")
    override val abbreviation: String?
        get() = TODO("Not yet implemented")
    override val initials: String
        get() = TODO("Not yet implemented")
    override val osisID: String?
        get() = TODO("Not yet implemented")
    override val isSupported: Boolean
        get() = TODO("Not yet implemented")
    override val isEnciphered: Boolean
        get() = TODO("Not yet implemented")
    override val isLocked: Boolean
        get() = TODO("Not yet implemented")

    override fun unlock(unlockKey: String?): Boolean {
        TODO("Not yet implemented")
    }

    override val unlockKey: String?
        get() = TODO("Not yet implemented")
    override val isQuestionable: Boolean
        get() = TODO("Not yet implemented")
    override val driverName: String?
        get() = TODO("Not yet implemented")
    override val isLeftToRight: Boolean
        get() = TODO("Not yet implemented")

    override fun reload() {
        TODO("Not yet implemented")
    }

    override val propertyKeys: Set<String?>?
        get() = TODO("Not yet implemented")

    override fun getProperty(key: String?): String? {
        TODO("Not yet implemented")
    }

    override fun setProperty(key: String?, value: String?) {
        TODO("Not yet implemented")
    }

    override fun putProperty(key: String?, value: String?) {
        TODO("Not yet implemented")
    }

    override fun putProperty(key: String?, value: String?, forFrontend: Boolean) {
        TODO("Not yet implemented")
    }

    override fun compareTo(other: BookMetaData?): Int {
        TODO("Not yet implemented")
    }
}