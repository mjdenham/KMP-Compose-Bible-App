package com.martin.bibleapp.ui.selector

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.martin.bibleapp.domain.reference.BibleBook
import com.martin.bibleapp.domain.reference.Reference
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalTestApi::class)
class BookSelectorKtTest {

    @Test
    fun testReferenceSelection() = runComposeUiTest {
        var clickedRef: Reference? = null
        setContent {
            ShowSelector(SelectorViewModel(), Modifier) { ref ->
                clickedRef = ref
            }
        }

        onNodeWithText("Ps")
            .assertExists("Gen does not exist")
            .performClick()

        onNodeWithText("23")
            .assertExists("Chapter 23 does not exist")
            .performClick()

        assertNotNull(clickedRef, "Reference not selected")
        assertEquals(BibleBook.PS, clickedRef!!.book)
        assertEquals(23, clickedRef!!.chapter)
    }
}

