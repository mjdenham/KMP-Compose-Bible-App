package com.martin.bibleapp.domain.osisconverter

import org.crosswire.jsword.passage.Key
import org.crosswire.jsword.passage.KeyText
import org.crosswire.jsword.passage.Verse
import org.kobjects.ktxml.api.EventType
import org.kobjects.ktxml.api.XmlPullParser
import org.kobjects.ktxml.mini.MiniXmlPullParser

class OsisToHtml {

    fun convertToHtml(osisList: List<KeyText>): String {
        return osisList
            .map { convertToHtml(it.key, it.text) }
            .joinToString("")
    }

    fun convertToHtml(key: Key, osisText: String): String {
        val parser: XmlPullParser = MiniXmlPullParser(
            osisText,
            false,
            true
        )

        val writeState = WriteState()
        var verseAdded = false
        val html = StringBuilder()
        while (parser.eventType != EventType.END_DOCUMENT) {
            when (parser.eventType) {
                EventType.START_TAG -> {
                    if (parser.name == "div" && writeState.canWrite() && parser.getAttributeValue("", "type") == "x-p") {
                        if (parser.getAttributeValue("", "sID") != null) {
                            html.append("<p>")
                        } else if (parser.getAttributeValue("", "eID") != null) {
                            html.append("</p>")
                        }
                    }

                    writeState.openTag(parser.name)
                }
                EventType.END_TAG -> {
                    writeState.closeTag()
                }
                EventType.TEXT -> {
                    if (writeState.canWrite()) {
                        val text = parser.text
                        if (!verseAdded && text.isNotBlank()) {
                            verseAdded = true
                            html.append(getVerseHtml(key))
                        }
                        html.append(text)
                    }
                }
                else -> {}
            }
            parser.next()
        }

        return html.toString()
    }

    private fun getVerseHtml(key: Key): String =
        if (key is Verse) {
            "<span class='verse-no' id='${key.getOsisID()}'>${key.verse}</span>&nbsp;"
        } else {
            ""
        }
}