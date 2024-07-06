package com.martin.bibleapp.data.usfm

import androidx.annotation.VisibleForTesting
import bibleapp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

class UsfmFileReader {
    @OptIn(ExperimentalResourceApi::class)
    suspend fun readFile(): String {
        return Res.readBytes("files/bsb/usfm/GEN.usfm")
            .decodeToString()
            .split("\n")
            .map { toHtml(it) }
            .filter { it.isNotEmpty() }
            .joinToString("")
    }

    @VisibleForTesting
    fun toHtml(line: String): String {
        if (line.isEmpty()) return ""
        val cleanLine = line.replace("\\\\f.*\\\\f\\*".toRegex(), "")
        val tag = cleanLine.substringBefore(" ")
        return when (tag) {
            "\\v" -> cleanLine.drop(3)
            "\\b" -> "<br />"
            else -> ""
        }
    }
}