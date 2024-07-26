package com.martin.bibleapp.data.repository.usfm

import androidx.annotation.VisibleForTesting
import bibleapp.composeapp.generated.resources.Res
import com.martin.bibleapp.domain.bible.BibleReader
import com.martin.bibleapp.domain.reference.BibleBook
import com.martin.bibleapp.domain.reference.Reference
import org.jetbrains.compose.resources.ExperimentalResourceApi

class UsfmFileReader : BibleReader {
    override suspend fun read(reference: Reference): String {
        val ref = CurrentReference(reference.book)
        return readLines(reference.book)
            .dropWhile { it != "\\c ${reference.chapter}" }
            .takeWhile { it != "\\c ${reference.chapter + 1}" }
            .map { toHtml(it, ref) }
            .filter { it.isNotEmpty() }
            .joinToString("")
    }

    override suspend fun countChapters(book: BibleBook): Int =
        readLines(book).count { it.startsWith("\\c") }

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun readLines(book: BibleBook) =
        Res.readBytes("files/bsb/usfm/${book.usfmCode}.usfm")
            .decodeToString()
            .split("\n")
            .asSequence()


    @VisibleForTesting
    fun toHtml(line: String, ref: CurrentReference): String {
        if (line.isEmpty()) return ""

        val (tag, remainder) = takeFirstValue(line)
        val cleanLine = removeFootnotes(remainder)
        return when (tag) {
            "\\v" -> {
                val (no, text) = takeFirstValue(cleanLine)
                ref.verse = no.toIntOrNull() ?: (ref.verse + 1)
                "<a id='${ref.getReference()}' /><small>$no</small> $text"
            }
            "\\h" -> "<h2>$cleanLine</h2>"
            "\\s1", "\\s2", "\\s3" -> "<p><i>$cleanLine</i></p>"
            "\\c" -> {
                ref.chapter = cleanLine.toIntOrNull() ?: (ref.chapter + 1)
                "<a id='${ref.getChapterReference()}' /><h3>Chapter $cleanLine</h3>"
            }
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

    data class CurrentReference(val book: BibleBook, var chapter: Int = 1, var verse: Int = 1) {
        fun getReference() = Reference.toCode(book, chapter, verse)
        fun getChapterReference() = Reference.toCode(book, chapter)
    }
}