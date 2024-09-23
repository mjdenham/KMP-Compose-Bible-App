package com.martin.bibleapp.ui.Test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TestViewModel(): ViewModel() {
    private val _output = MutableStateFlow<List<String>>(emptyList())
    val output = _output.asStateFlow()

    fun testWriteFile() {
        viewModelScope.launch {
            _output.update {
                listOf("writing file") + output.value
            }
        }
    }
}