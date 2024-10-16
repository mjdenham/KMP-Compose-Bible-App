package org.crosswire.jsword.book.sword

import org.crosswire.jsword.book.sword.state.ZVerseBackendState
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.passage.VerseRange
import org.crosswire.jsword.versification.BibleBook
import org.crosswire.jsword.versification.system.Versifications
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertContains

class ZVerseBackendTest {

    private var backendState: ZVerseBackendState = ZVerseBackendState(SwordBookMetaData(), BlockType.BLOCK_BOOK)
    private var backend = ZVerseBackend()

    @BeforeTest
    fun setup() {
    }

    @Test
    fun readRawContent_readFirstVerse() {
        val v11nName = "KJV" //getBookMetaData().getProperty(BookMetaData.KEY_VERSIFICATION);
        val v11n = Versifications.instance().getVersification(v11nName)
        val result = backend.readRawContent(backendState, Verse(v11n, BibleBook.GEN, 1, 1))
        kotlin.test.assertEquals(
            "<div type=\"x-milestone\" subType=\"x-preverse\" sID=\"pv1\"/><title type=\"parallel\"> (<reference osisRef=\"John.1.1-John.1.5\" type=\"parallel\">John 1:1–5</reference>; <reference osisRef=\"Heb.11.1-Heb.11.3\" type=\"parallel\">Hebrews 11:1–3</reference>) </title> <div sID=\"gen17\" type=\"x-p\"/><div type=\"x-milestone\" subType=\"x-preverse\" eID=\"pv1\"/><w lemma=\"lemma.BSBlex:בְּרֵאשִׁ֖ית strong:H7225\" xlit=\"Latn:bə·rê·šîṯ\">In the beginning</w> <w lemma=\"lemma.BSBlex:אֱלֹהִ֑ים strong:H430\" xlit=\"Latn:’ĕ·lō·hîm\">God</w><w lemma=\"lemma.BSBlex:אֵ֥ת strong:H853\" xlit=\"Latn:’êṯ\"/> <w lemma=\"lemma.BSBlex:בָּרָ֣א strong:H1254\" xlit=\"Latn:bā·rā\">created</w> <w lemma=\"lemma.BSBlex:הַשָּׁמַ֖יִם strong:H8064\" xlit=\"Latn:haš·šā·ma·yim\">the heavens</w> <w lemma=\"lemma.BSBlex:וְאֵ֥ת strong:H853\" xlit=\"Latn:wə·’êṯ\">and</w> <w lemma=\"lemma.BSBlex:הָאָֽרֶץ׃ strong:H776\" xlit=\"Latn:hā·’ā·reṣ\">the earth</w>. <div eID=\"gen17\" type=\"x-p\"/>",
            result
        )
    }

    @Test
    fun readRawContent_readLastVerseInChapter() {
        val v11nName = "KJV" //getBookMetaData().getProperty(BookMetaData.KEY_VERSIFICATION);
        val v11n = Versifications.instance().getVersification(v11nName)
        val result = backend.readRawContent(backendState, Verse(v11n, BibleBook.GEN, 1, 31))
        listOf("And God looked upon all that he had made and indeed it was very good".split(" ").forEach { word: String ->
            assertContains(result, word)
        })
    }

    @Test
    fun readRawContent_readNtVerse() {
        val v11nName = "KJV" //getBookMetaData().getProperty(BookMetaData.KEY_VERSIFICATION);
        val v11n = Versifications.instance().getVersification(v11nName)
        val result = backend.readRawContent(backendState, Verse(v11n, BibleBook.JOHN, 1, 1))
        println(result)
        listOf("In the beginning ws the Word and the Word was with God and the Word was God".split(" ").forEach { word: String ->
            assertContains(result, word)
        })
    }

    @Test
    fun readRawContent_readChapter() {
        val v11nName = "KJV" //getBookMetaData().getProperty(BookMetaData.KEY_VERSIFICATION);
        val v11n = Versifications.instance().getVersification(v11nName)
        val start = Verse(v11n, BibleBook.GEN, 1, 1)
        val end = Verse(v11n, BibleBook.GEN, 1, 31)
        val result = backend.readRawContent(backendState, VerseRange(v11n, start, end))
        listOf("In the beginning God created the heavens and the earth".split(" ").forEach { word: String ->
            assertContains(result, word)
        })
    }
}