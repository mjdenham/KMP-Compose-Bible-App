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

import org.crosswire.jsword.book.Book
import org.crosswire.jsword.book.BookCategory
import org.crosswire.jsword.book.KeyType
import org.crosswire.jsword.book.sword.state.ZVerseBackendState

/**
 * Data about book types.
 *
 * @see gnu.lgpl.License The GNU Lesser General Public License for details.
 *
 * @author Joe Walker
 * @author DM Smith
 */
enum class BookType(
    val nameInConfig: String, category: BookCategory, type: KeyType
) {
    /**
     * Uncompressed Bibles
     */
//    RAW_TEXT("RawText", BookCategory.BIBLE, KeyType.VERSE) {
//        override fun getBook(sbmd: SwordBookMetaData?, backend: Backend?): Book {
//            return SwordBook(sbmd, backend)
//        }
//
//        override fun getBackend(sbmd: SwordBookMetaData?): Backend {
//            return RawBackend(sbmd, 2)
//        }
//    },

    /**
     * Compressed Bibles
     */
    Z_TEXT("zText", BookCategory.BIBLE, KeyType.VERSE) {
        override fun getBook(sbmd: SwordBookMetaData): Book {
            return SwordBook(sbmd, getBackend(sbmd))
        }

        override fun getBackend(sbmd: SwordBookMetaData): Backend<ZVerseBackendState> {
            val blockType = BlockType.BLOCK_BOOK //BlockType.fromString(sbmd.getProperty(SwordBookMetaData.KEY_BLOCK_TYPE))
            return ZVerseBackend(sbmd, blockType, 2)
        }
    },

//    /**
//     * Compressed Bibles
//     */
//    Z_TEXT4("zText4", BookCategory.BIBLE, KeyType.VERSE) {
//        override fun getBook(sbmd: SwordBookMetaData?, backend: Backend?): Book {
//            return SwordBook(sbmd, backend)
//        }
//
//        @Throws(BookException::class)
//        protected override fun getBackend(sbmd: SwordBookMetaData): Backend {
//            val blockType = BlockType.fromString(sbmd.getProperty(SwordBookMetaData.KEY_BLOCK_TYPE))
//            return ZVerseBackend(sbmd, blockType, 4)
//        }
//    },
//
//    /**
//     * Uncompressed Commentaries
//     */
//    RAW_COM("RawCom", BookCategory.COMMENTARY, KeyType.VERSE) {
//        override fun getBook(sbmd: SwordBookMetaData?, backend: Backend?): Book {
//            return SwordBook(sbmd, backend)
//        }
//
//        @Throws(BookException::class)
//        override fun getBackend(sbmd: SwordBookMetaData?): Backend {
//            return RawBackend(sbmd, 2)
//        }
//    },
//
//    RAW_COM4("RawCom4", BookCategory.COMMENTARY, KeyType.VERSE) {
//        override fun getBook(sbmd: SwordBookMetaData?, backend: Backend?): Book {
//            return SwordBook(sbmd, backend)
//        }
//
//        @Throws(BookException::class)
//        override fun getBackend(sbmd: SwordBookMetaData?): Backend {
//            return RawBackend(sbmd, 4)
//        }
//    },
//
    /**
     * Compressed Commentaries
     */
    Z_COM("zCom", BookCategory.COMMENTARY, KeyType.VERSE) {
        override fun getBook(sbmd: SwordBookMetaData): Book {
            return SwordBook(sbmd, getBackend(sbmd))
        }

        override fun getBackend(sbmd: SwordBookMetaData): Backend<ZVerseBackendState> {
            val blockType = BlockType.BLOCK_BOOK //BlockType.fromString(sbmd.getProperty(SwordBookMetaData.KEY_BLOCK_TYPE))
            return ZVerseBackend(sbmd, blockType, 2)
        }
    };

//    /**
//     * Compressed Commentaries
//     */
//    Z_COM4("zCom4", BookCategory.COMMENTARY, KeyType.VERSE) {
//        override fun getBook(sbmd: SwordBookMetaData?, backend: Backend?): Book {
//            return SwordBook(sbmd, backend)
//        }
//
//        @Throws(BookException::class)
//        protected override fun getBackend(sbmd: SwordBookMetaData): Backend {
//            val blockType = BlockType.fromString(sbmd.getProperty(SwordBookMetaData.KEY_BLOCK_TYPE))
//            return ZVerseBackend(sbmd, blockType, 4)
//        }
//    },
//
//    /**
//     * Uncompresses HREF Commentaries
//     */
//    HREF_COM("HREFCom", BookCategory.COMMENTARY, KeyType.VERSE) {
//        override fun getBook(sbmd: SwordBookMetaData?, backend: Backend?): Book {
//            return SwordBook(sbmd, backend)
//        }
//
//        @Throws(BookException::class)
//        override fun getBackend(sbmd: SwordBookMetaData?): Backend {
//            return RawBackend(sbmd, 2)
//        }
//    },
//
//    /**
//     * Uncompressed Commentaries
//     */
//    RAW_FILES("RawFiles", BookCategory.COMMENTARY, KeyType.VERSE) {
//        override fun getBook(sbmd: SwordBookMetaData?, backend: Backend?): Book {
//            return SwordBook(sbmd, backend)
//        }
//
//        @Throws(BookException::class)
//        override fun getBackend(sbmd: SwordBookMetaData?): Backend {
//            return RawFileBackend(sbmd, 2)
//        }
//    },
//
//    /**
//     * 2-Byte Index Uncompressed Dictionaries
//     */
//    RAW_LD("RawLD", BookCategory.DICTIONARY, KeyType.LIST) {
//        protected override fun getBook(sbmd: SwordBookMetaData, backend: Backend?): Book {
//            if (sbmd.getBookCategory().equals(BookCategory.DAILY_DEVOTIONS)) {
//                return SwordDailyDevotion(sbmd, backend)
//            }
//            return SwordDictionary(sbmd, backend)
//        }
//
//        @Throws(BookException::class)
//        override fun getBackend(sbmd: SwordBookMetaData?): Backend {
//            return RawLDBackend(sbmd, 2)
//        }
//    },
//
//    /**
//     * 4-Byte Index Uncompressed Dictionaries
//     */
//    RAW_LD4("RawLD4", BookCategory.DICTIONARY, KeyType.LIST) {
//        protected override fun getBook(sbmd: SwordBookMetaData, backend: Backend?): Book {
//            if (sbmd.getBookCategory().equals(BookCategory.DAILY_DEVOTIONS)) {
//                return SwordDailyDevotion(sbmd, backend)
//            }
//            return SwordDictionary(sbmd, backend)
//        }
//
//        @Throws(BookException::class)
//        override fun getBackend(sbmd: SwordBookMetaData?): Backend {
//            return RawLDBackend(sbmd, 4)
//        }
//    },
//
//    /**
//     * Compressed Dictionaries
//     */
//    Z_LD("zLD", BookCategory.DICTIONARY, KeyType.LIST) {
//        protected override fun getBook(sbmd: SwordBookMetaData, backend: Backend?): Book {
//            if (sbmd.getBookCategory().equals(BookCategory.DAILY_DEVOTIONS)) {
//                return SwordDailyDevotion(sbmd, backend)
//            }
//            return SwordDictionary(sbmd, backend)
//        }
//
//        @Throws(BookException::class)
//        override fun getBackend(sbmd: SwordBookMetaData?): Backend {
//            return ZLDBackend(sbmd)
//        }
//    },
//
//    /**
//     * Generic Books
//     */
//    RAW_GEN_BOOK("RawGenBook", BookCategory.GENERAL_BOOK, KeyType.TREE) {
//        override fun getBook(sbmd: SwordBookMetaData?, backend: Backend?): Book {
//            return SwordGenBook(sbmd, backend)
//        }
//
//        @Throws(BookException::class)
//        override fun getBackend(sbmd: SwordBookMetaData?): Backend {
//            return GenBookBackend(sbmd)
//        }
//    };

    val bookCategory: BookCategory?
        /**
         * The category of this book
         *
         * @return the category of this book
         */
        get() = category

    /**
     * Given a SwordBookMetaData determine whether this BookType will work for
     * it.
     *
     * @param sbmd
     * the BookMetaData that this BookType works upon
     * @return true if this is a usable BookType
     */
    fun isSupported(sbmd: SwordBookMetaData?): Boolean {
        return category != null && sbmd != null
    }

    /**
     * Create a Book appropriate for the BookMetaData
     *
     * @param sbmd the book metadata
     * @return a book for that metadata
     * @throws BookException
     */
//    fun createBook(sbmd: SwordBookMetaData?): Book {
//        return getBook(sbmd, getBackend(sbmd))
//    }

    /**
     * Create a Book with the given backend
     */
    abstract fun getBook(sbmd: SwordBookMetaData): Book

    /**
     * Create a the appropriate backend for this type of book
     */
    protected abstract fun getBackend(sbmd: SwordBookMetaData): Backend<*>?

    /**
     * What category is this book
     */
    private val category: BookCategory = category

    /**
     * Get the way this type of Book organizes it's keys.
     *
     * @return the organization of keys for this book
     */
    /**
     * What category is this book
     */
    val keyType: KeyType = type

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    override fun toString(): String {
        return nameInConfig
    }

    companion object {
        /**
         * Find a BookType from a name.
         *
         * @param name
         * The name of the BookType to look up
         * @return The found BookType or null if the name is not found
         */
        fun getBookType(name: String?): BookType {
            for (v in entries) {
                if (v.nameInConfig.equals(name, ignoreCase = true)) {
                    return v
                }
            }

            throw IllegalArgumentException(
//                JSOtherMsg.lookupText(
                    "BookType $name is not defined!",
//                )
            )
        }

        /**
         * Lookup method to convert from a String
         *
         * @param name the string representation of a book type
         * @return the matching book type
         */
        fun fromString(name: String?): BookType {
            for (v in entries) {
                if (v.nameInConfig.equals(name, ignoreCase = true)) {
                    return v
                }
            }

            throw ClassCastException(
//                JSOtherMsg.lookupText(
                    "DataType $name is not defined!",
//                )
            )
        }
    }
}
