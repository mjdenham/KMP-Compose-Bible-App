package com.martin.bibleapp.domain.osisconverter

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import kotlin.test.assertContains

class OsisToHtmlTest {

    private lateinit var osisToHtml: OsisToHtml

    @Before
    fun setUp() {
        osisToHtml = OsisToHtml()
    }

    @Test
    fun convertToHtml_should_show_basic_text_with_separators() {
        val html = osisToHtml.convertToHtml(GEN_1_OSIS)
        assertContains(html, "In the beginning God created the heavens and the earth")
    }

    @Test
    fun convertToHtml_should_have_paragraphs() {
        val html = osisToHtml.convertToHtml(GEN_1_OSIS)
        assertContains(html, "<p>In the beginning God created the heavens and the earth. </p>")
    }

    companion object {
        val GEN_1_OSIS = "<container><div type=\"x-milestone\" subType=\"x-preverse\" sID=\"pv1\"/><title type=\"parallel\"> (<reference osisRef=\"John.1.1-John.1.5\" type=\"parallel\">John 1:1–5</reference>; <reference osisRef=\"Heb.11.1-Heb.11.3\" type=\"parallel\">Hebrews 11:1–3</reference>) </title> <div sID=\"gen17\" type=\"x-p\"/><div type=\"x-milestone\" subType=\"x-preverse\" eID=\"pv1\"/><w lemma=\"lemma.BSBlex:בְּרֵאשִׁ֖ית strong:H7225\" xlit=\"Latn:bə·rê·šîṯ\">In the beginning</w> <w lemma=\"lemma.BSBlex:אֱלֹהִ֑ים strong:H430\" xlit=\"Latn:’ĕ·lō·hîm\">God</w><w lemma=\"lemma.BSBlex:אֵ֥ת strong:H853\" xlit=\"Latn:’êṯ\"/> <w lemma=\"lemma.BSBlex:בָּרָ֣א strong:H1254\" xlit=\"Latn:bā·rā\">created</w> <w lemma=\"lemma.BSBlex:הַשָּׁמַ֖יִם strong:H8064\" xlit=\"Latn:haš·šā·ma·yim\">the heavens</w> <w lemma=\"lemma.BSBlex:וְאֵ֥ת strong:H853\" xlit=\"Latn:wə·’êṯ\">and</w> <w lemma=\"lemma.BSBlex:הָאָֽרֶץ׃ strong:H776\" xlit=\"Latn:hā·’ā·reṣ\">the earth</w>. <div eID=\"gen17\" type=\"x-p\"/>, <div type=\"x-milestone\" subType=\"x-preverse\" sID=\"pv2\"/><div sID=\"gen18\" type=\"x-p\"/><div type=\"x-milestone\" subType=\"x-preverse\" eID=\"pv2\"/><w lemma=\"lemma.BSBlex:וְהָאָ֗רֶץ strong:H776\" xlit=\"Latn:wə·hā·’ā·reṣ\">Now the earth</w> <w lemma=\"lemma.BSBlex:הָיְתָ֥ה strong:H1961\" xlit=\"Latn:hā·yə·ṯāh\">was</w> <w lemma=\"lemma.BSBlex:תֹ֙הוּ֙ strong:H8414\" xlit=\"Latn:ṯō·hū\">formless</w> <w lemma=\"lemma.BSBlex:וָבֹ֔הוּ strong:H922\" xlit=\"Latn:wā·ḇō·hū\">and void</w>, <w lemma=\"lemma.BSBlex:וְחֹ֖שֶׁךְ strong:H2822\" xlit=\"Latn:wə·ḥō·šeḵ\">and darkness</w> <w lemma=\"lemma.BSBlex:עַל־ strong:H5921\" xlit=\"Latn:‘al-\">was over</w> <w lemma=\"lemma.BSBlex:פְּנֵ֣י strong:H6440\" xlit=\"Latn:pə·nê\">the surface</w> <w lemma=\"lemma.BSBlex:תְה֑וֹם strong:H8415\" xlit=\"Latn:ṯə·hō·wm\">of the deep</w>. <w lemma=\"lemma.BSBlex:וְר֣וּחַ strong:H7307\" xlit=\"Latn:wə·rū·aḥ\">And the Spirit</w> <w lemma=\"lemma.BSBlex:אֱלֹהִ֔ים strong:H430\" xlit=\"Latn:’ĕ·lō·hîm\">of God</w> <w lemma=\"lemma.BSBlex:מְרַחֶ֖פֶת strong:H7363\" xlit=\"Latn:mə·ra·ḥe·p̄eṯ\">was hovering</w> <w lemma=\"lemma.BSBlex:עַל־ strong:H5921\" xlit=\"Latn:‘al-\">over</w> <w lemma=\"lemma.BSBlex:פְּנֵ֥י strong:H6440\" xlit=\"Latn:pə·nê\">the surface</w> <w lemma=\"lemma.BSBlex:הַמָּֽיִם׃ strong:H4325\" xlit=\"Latn:ham·mā·yim\">of the waters</w>. <div eID=\"gen18\" type=\"x-p\"/>, <div type=\"x-milestone\" subType=\"x-preverse\" sID=\"pv3\"/><title>The First Day</title> <div sID=\"gen19\" type=\"x-p\"/><div type=\"x-milestone\" subType=\"x-preverse\" eID=\"pv3\"/><w lemma=\"lemma.BSBlex:אֱלֹהִ֖ים strong:H430\" xlit=\"Latn:’ĕ·lō·hîm\">And God</w> <w lemma=\"lemma.BSBlex:וַיֹּ֥אמֶר strong:H559\" xlit=\"Latn:way·yō·mer\">said</w>, “<w lemma=\"lemma.BSBlex:יְהִ֣י strong:H1961\" xlit=\"Latn:yə·hî\">Let there be</w> <w lemma=\"lemma.BSBlex:א֑וֹר strong:H216\" xlit=\"Latn:’ō·wr\">light</w>,”<note placement=\"foot\">Cited in <reference osisRef=\"2Cor.4.6\" type=\"source\">2 Corinthians 4:6</reference></note> <w lemma=\"lemma.BSBlex:וַֽיְהִי־ strong:H1961\" xlit=\"Latn:way·hî-\">and there was</w> <w lemma=\"lemma.BSBlex:אֽוֹר׃ strong:H216\" xlit=\"Latn:’ō·wr\">light</w>. , <w lemma=\"lemma.BSBlex:אֱלֹהִ֛ים strong:H430\" xlit=\"Latn:’ĕ·lō·hîm\">And God</w><w lemma=\"lemma.BSBlex:אֶת־ strong:H853\" xlit=\"Latn:’eṯ-\"/> <w lemma=\"lemma.BSBlex:וַיַּ֧רְא strong:H7200\" xlit=\"Latn:way·yar\">saw</w> <w lemma=\"lemma.BSBlex:כִּי־ strong:H3588\" xlit=\"Latn:kî-\">that</w> <w lemma=\"lemma.BSBlex:הָא֖וֹר strong:H216\" xlit=\"Latn:hā·’ō·wr\">the light</w> was <w lemma=\"lemma.BSBlex:ט֑וֹב strong:H2896\" xlit=\"Latn:ṭō·wḇ\">good</w>, <w lemma=\"lemma.BSBlex:אֱלֹהִ֔ים strong:H430\" xlit=\"Latn:’ĕ·lō·hîm\">and</w> He <w lemma=\"lemma.BSBlex:וַיַּבְדֵּ֣ל strong:H914\" xlit=\"Latn:way·yaḇ·dêl\">separated</w></container>"
    }
}