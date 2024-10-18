package org.crosswire.jsword.book.sword

import okio.Path
import okio.Path.Companion.toPath
import okio.use
import org.crosswire.jsword.book.BookMetaData
import org.crosswire.jsword.book.KeyType
import org.crosswire.jsword.book.sword.state.OpenFileState
import org.crosswire.jsword.passage.Key
import org.crosswire.jsword.passage.KeyUtil.getPassage
import org.crosswire.jsword.passage.KeyUtil.getVerse
import org.crosswire.jsword.passage.RestrictionType
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.passage.VerseRange

abstract class AbstractBackend<T: OpenFileState>(val bmd: SwordBookMetaData) : StatefulFileBackedBackend<T>, Backend<T> {

    fun readToOsis(key: Key): List<String> {

        initState().use { openFileState ->
            val content = when (this.bmd.getKeyType()) {
                KeyType.LIST -> readNormalOsis(key, openFileState)
//                TREE -> readNormalOsisSingleKey(key, processor, content, openFileState)
                KeyType.VERSE -> readPassageOsis(key, openFileState)
                else -> throw /*Book*/Exception("Book has unsupported type of key")
            }
            return content
        }
    }

    private fun readNormalOsis(
        key: Key,
//        processor: RawTextToXmlProcessor,
//        content: List<org.jdom2.Content>,
        openFileState: T
    ): List<String> {
        val contentList = mutableListOf<String>()
        // simply lookup the key and process the relevant information
        val iterator = key.iterator()

        while (iterator.hasNext()) {
            val next = iterator.next()
            var rawText: String?
            try {
                rawText = readRawContent(openFileState, next)
                contentList.add(rawText)
//                processor.postVerse(next, content, rawText)
            } catch (e: Exception) {
                // failed to process key 'next'
//                throwFailedKeyException(key, next, e)
            }
        }
        return contentList
    }


    /**
     * Reads a passage as OSIS
     *
     * @param key           the given key
     * @param processor     a processor for which to do things with
     * @param content       a list of content to be appended to (i.e. the OSIS data)
     * @param openFileState the open file state, from which we read things
     * @throws BookException a book exception if we failed to read the book
     */
    private fun readPassageOsis(
        key: Key,
//        processor: RawTextToXmlProcessor,
//        content: List<org.jdom2.Content>,
        openFileState: T
    ): List<String> {
        val contentList = mutableListOf<String>()
        var currentVerse: Verse? = null
        val rit = when (key) {
            is VerseRange -> key.iterator()
            else -> getPassage(key).rangeIterator(RestrictionType.CHAPTER)
        }
        while (rit.hasNext()) {
            val range = rit.next()
//            processor.preRange(range, content)

            // FIXME(CJB): can this now be optimized since we can calculate
            // the buffer size of what to read?
            // now iterate through all verses in range
            for (verseInRange in range) {
                currentVerse = getVerse(verseInRange)
                try {
                    val rawText = readRawContent(openFileState, currentVerse)
                    contentList.add(rawText)
//                    processor.postVerse(verseInRange, content, rawText)
                } catch (e: Exception) {
                    //some versifications have more verses than modules contain - so can't throw
                    //an error here...
//                    AbstractBackend.LOGGER.debug(e.message, e)
                }
            }
        }

        return contentList
    }

    fun getExpandedDataPath(): Path /* URI */ {
//        val loc: java.net.URI = NetUtil.lengthenURI(
//            bmd.getLibrary(),
//            bmd.getProperty(ConfigEntryType.DATA_PATH) as String
//        )
//            ?: throw BookException(Msg.MISSING_FILE)
//
//        return loc
        return "/Users/martin/StudioProjects/kmp-sword/testFiles/BSB/modules/texts/ztext/bsb".toPath()
    }

    override fun getBookMetaData(): BookMetaData = bmd
}
