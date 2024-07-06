package com.martin.bibleapp.data

import com.martin.bibleapp.data.usfm.UsfmFileReader

class BibleData {
    suspend fun readPage(): String {
        return UsfmFileReader().readFile()
    }
}