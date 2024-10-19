package org.crosswire.jsword.book.sword

import org.crosswire.jsword.book.BookMetaData
import org.crosswire.jsword.book.BookMetaData.Companion.KEY_CATEGORY
import org.crosswire.jsword.book.BookMetaData.Companion.KEY_LANG
import org.crosswire.jsword.book.BookMetaData.Companion.KEY_VERSIFICATION
import org.crosswire.jsword.book.KeyType
import org.crosswire.jsword.book.basic.AbstractBookMetaData

class SwordBookMetaData: AbstractBookMetaData() {
    companion object {
        const val KEY_ABBREVIATION: String = "Abbreviation"
        const val KEY_ABOUT: String = "About"
        const val KEY_BLOCK_COUNT: String = "BlockCount"
        const val KEY_BLOCK_TYPE: String = "BlockType"
        const val KEY_CASE_SENSITIVE_KEYS: String = "CaseSensitiveKeys"
        const val KEY_CIPHER_KEY: String = "CipherKey"
        const val KEY_COMPRESS_TYPE: String = "CompressType"
        const val KEY_COPYRIGHT: String = "Copyright"
        const val KEY_COPYRIGHT_CONTACT_ADDRESS: String = "CopyrightContactAddress"
        const val KEY_COPYRIGHT_CONTACT_EMAIL: String = "CopyrightContactEmail"
        const val KEY_COPYRIGHT_CONTACT_NAME: String = "CopyrightContactName"
        const val KEY_COPYRIGHT_CONTACT_NOTES: String = "CopyrightContactNotes"
        const val KEY_COPYRIGHT_DATE: String = "CopyrightDate"
        const val KEY_COPYRIGHT_HOLDER: String = "CopyrightHolder"
        const val KEY_COPYRIGHT_NOTES: String = "CopyrightNotes"
        const val KEY_DATA_PATH: String = "DataPath"
        const val KEY_DESCRIPTION: String = "Description"
        const val KEY_DIRECTION: String = "Direction"
        const val KEY_DISPLAY_LEVEL: String = "DisplayLevel"
        const val KEY_DISTRIBUTION_LICENSE: String = "DistributionLicense"
        const val KEY_DISTRIBUTION_NOTES: String = "DistributionNotes"
        const val KEY_DISTRIBUTION_SOURCE: String = "DistributionSource"
        const val KEY_ENCODING: String = "Encoding"
        const val KEY_FEATURE: String = "Feature"
        const val KEY_GLOBAL_OPTION_FILTER: String = "GlobalOptionFilter"
        const val KEY_SIGLUM1: String = "Siglum1"
        const val KEY_SIGLUM2: String = "Siglum2"
        const val KEY_SIGLUM3: String = "Siglum3"
        const val KEY_SIGLUM4: String = "Siglum4"
        const val KEY_SIGLUM5: String = "Siglum5"
        const val KEY_GLOSSARY_FROM: String = "GlossaryFrom"
        const val KEY_GLOSSARY_TO: String = "GlossaryTo"
        const val KEY_HISTORY: String = "History"
        const val KEY_INSTALL_SIZE: String = "InstallSize"
        const val KEY_KEY_TYPE: String = "KeyType"
        const val KEY_LCSH: String = "LCSH"
        const val KEY_LOCAL_STRIP_FILTER: String = "LocalStripFilter"
        const val KEY_MINIMUM_VERSION: String = "MinimumVersion"
        const val KEY_MOD_DRV: String = "ModDrv"
        const val KEY_OBSOLETES: String = "Obsoletes"
        const val KEY_OSIS_Q_TO_TICK: String = "OSISqToTick"
        const val KEY_OSIS_VERSION: String = "OSISVersion"
        const val KEY_PREFERRED_CSS_XHTML: String = "PreferredCSSXHTML"
        const val KEY_SEARCH_OPTION: String = "SearchOption"
        const val KEY_SHORT_COPYRIGHT: String = "ShortCopyright"
        const val KEY_SHORT_PROMO: String = "ShortPromo"
        const val KEY_SOURCE_TYPE: String = "SourceType"
        const val KEY_STRONGS_PADDING: String = "StrongsPadding"
        const val KEY_SWORD_VERSION_DATE: String = "SwordVersionDate"
        const val KEY_TEXT_SOURCE: String = "TextSource"
        const val KEY_UNLOCK_URL: String = "UnlockURL"
        const val KEY_VERSION: String = "Version"

        // Some keys have defaults
        val DEFAULTS = mapOf(
            KEY_COMPRESS_TYPE to "ZIP",
            KEY_BLOCK_TYPE to "BOOK",
            KEY_BLOCK_COUNT to "200",
            KEY_KEY_TYPE to "Verse",
            KEY_VERSIFICATION to "KJV",
            KEY_DIRECTION to "LtoR",
            KEY_SOURCE_TYPE to "Plaintext",
            KEY_ENCODING to "UTF-8",
            KEY_DISPLAY_LEVEL to "1",
            KEY_OSIS_Q_TO_TICK to "true",
            KEY_VERSION to "1.0",
            KEY_MINIMUM_VERSION to "1.5.1a",
            KEY_CATEGORY to "Biblical Texts",
            KEY_LANG to "en",
            KEY_DISTRIBUTION_LICENSE to "Public Domain",
            KEY_CASE_SENSITIVE_KEYS to "false",
            KEY_STRONGS_PADDING to "true"
        )
    }
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

    private val configAll: MutableMap<String, String> = HashMap()

    override fun getProperty(key: String): String? {
//        if (BookMetaData.KEY_LANGUAGE == key) {
//            return getLanguage().getName()
//        }
//        return configAll.get(key, SwordBookMetaData.DEFAULTS.get(key))
        return configAll[key]
    }

    /* (non-Javadoc)
     * @see org.crosswire.jsword.book.basic.AbstractBookMetaData#setProperty(java.lang.String, java.lang.String)
     */
    override fun setProperty(key: String, value: String) {
//        configAll.replace(key, value)
        configAll[key] = value
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