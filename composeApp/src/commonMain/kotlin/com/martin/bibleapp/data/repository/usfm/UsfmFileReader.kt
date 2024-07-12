package com.martin.bibleapp.data.repository.usfm

import androidx.annotation.VisibleForTesting
import bibleapp.composeapp.generated.resources.Res
import com.martin.bibleapp.domain.bible.BibleReader
import com.martin.bibleapp.domain.reference.BibleBook
import org.jetbrains.compose.resources.ExperimentalResourceApi

class UsfmFileReader : BibleReader {
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun read(book: BibleBook): String {
        return Res.readBytes("files/bsb/usfm/${book.usfmCode}.usfm")
            .decodeToString()
            .split("\n")
            .map { toHtml(it) }
            .filter { it.isNotEmpty() }
            .joinToString("")
    }

    @VisibleForTesting
    fun toHtml(line: String): String {
        if (line.isEmpty()) return ""

        val (tag, remainder) = takeFirstValue(line)
        val cleanLine = removeFootnotes(remainder)
        return when (tag) {
            "\\v" -> {
                val (no, text) = takeFirstValue(cleanLine)
                "<small>$no</small> $text"
            }
            "\\h" -> "<h2>$cleanLine</h2>"
            "\\s1", "\\s2", "\\s3" -> "<p><i>$cleanLine</i></p>"
            "\\c" -> "<h3>Chapter $cleanLine</h3>"
            "\\q1" -> "<p>$cleanLine</p>"
            "\\q2" -> "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$cleanLine</p>"
            "\\b" -> "<br />"
            else -> ""
        }
    }

    private fun takeFirstValue(line: String): Pair<String, String> {
        val parts = line.split("[\\n\\r\\s]+".toRegex(), 2)
        val tag = parts.getOrNull(0).orEmpty()
        val remainder = parts.getOrNull(1).orEmpty()
        return Pair(tag, remainder)
    }

    private fun removeFootnotes(line: String) = line.replace("\\\\f.*\\\\f\\*".toRegex(), "")
}