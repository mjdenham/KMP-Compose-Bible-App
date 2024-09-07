package com.martin.bibleapp.data.file

import android.content.Context
import com.martin.bibleapp.domain.file.FileProvider

class AndroidFileProvider(private val context: Context): FileProvider {

    override fun getFilePath(fileName: String): String = context.filesDir.resolve(fileName).absolutePath
}