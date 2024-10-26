package com.martin.bibleapp.domain.osisconverter

import org.kobjects.ktxml.api.EventType
import org.kobjects.ktxml.api.XmlPullParser
import org.kobjects.ktxml.mini.MiniXmlPullParser

class OsisToText {
    fun convertToText(osisText: String): String {
        val parser: XmlPullParser = MiniXmlPullParser(
            osisText,
            false,
            true
        )
        val shouldAddTextStack = ArrayDeque<Boolean>().apply {
            addLast(true)
        }

        val result = StringBuilder()
        while (parser.eventType != EventType.END_DOCUMENT) {
            when (parser.eventType) {
                EventType.START_TAG -> {
                    shouldAddTextStack.addLast(
                        parser.name in listOf("w", "container")
                    )
                }
                EventType.END_TAG -> {
                    shouldAddTextStack.removeLast()
                }
                EventType.TEXT -> {
                    if (shouldAddTextStack.last()) {
                        result.append(parser.text)
                    }
                }
                else -> {}
            }
            parser.next()
        }

        return result.toString()
    }
}