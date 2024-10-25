package com.martin.bibleapp.domain.osisconverter

import org.kobjects.ktxml.api.EventType
import org.kobjects.ktxml.api.XmlPullParser
import org.kobjects.ktxml.mini.MiniXmlPullParser

class OsisToHtml {
    fun convertToHtml(osisText: String): String {
        val parser: XmlPullParser = MiniXmlPullParser(
            osisText,
            false,
            true
        )

        val shouldAddTextStack = ArrayDeque<Boolean>()
        val html = StringBuilder()
        while (parser.eventType != EventType.END_DOCUMENT) {
            when (parser.eventType) {
                EventType.START_TAG -> {
                    if (parser.name == "div" && shouldAddTextStack.last() && parser.getAttributeValue("", "type") == "x-p") {
                        if (parser.getAttributeValue("", "sID") != null) {
                            html.append("<p>")
                        } else if (parser.getAttributeValue("", "eID") != null) {
                            html.append("</p>")
                        }
                    }

                    shouldAddTextStack.addLast(
                        parser.name in listOf("w", "container")
                    )
                }
                EventType.END_TAG -> {
                    shouldAddTextStack.removeLast()
                }
                EventType.TEXT -> {
                    if (shouldAddTextStack.last()) {
                        html.append(parser.text)
                    }
                }
                else -> {}
            }
            parser.next()
        }

        return html.toString()
    }
}