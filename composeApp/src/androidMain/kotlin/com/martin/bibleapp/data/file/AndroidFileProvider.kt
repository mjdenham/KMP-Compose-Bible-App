package com.martin.bibleapp.data.file

import android.content.Context
import com.martin.bibleapp.domain.file.FileProvider

class AndroidFileProvider(private val context: Context): FileProvider {

    // try to use external storage first, if not available use internal storage
    override fun getFilePath(fileName: String): String = context.getExternalFilesDir(null)?.absolutePath?.plus("/$fileName")
        ?: context.filesDir.resolve(fileName).absolutePath
}