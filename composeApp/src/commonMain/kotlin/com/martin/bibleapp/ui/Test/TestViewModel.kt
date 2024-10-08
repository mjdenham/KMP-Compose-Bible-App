package com.martin.bibleapp.ui.Test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.file.FileProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

class TestViewModel(val fp: FileProvider): ViewModel() {
    private val _output = MutableStateFlow<List<String>>(emptyList())
    val output = _output.asStateFlow()

    fun testReadSwordBsb() {
        viewModelScope.launch {
            printOut("Reading Sword BSB TBD")

            printOut("Read")
        }
    }

    private fun printOut(out: String) {
        _output.update {
            output.value + out
        }
    }
}