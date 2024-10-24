package com.martin.bibleapp.ui.selector

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.martin.bibleapp.data.repository.usfm.UsfmFileReader
import com.martin.bibleapp.domain.bible.ReferenceSelectionUseCase
import org.crosswire.jsword.versification.BibleBook
import com.martin.bibleapp.fakes.FakeCurrentReferenceRepository
import com.martin.bibleapp.ui.util.OrientationProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalTestApi::class)
class BookAndChapterSelectorTest {

    @Test
    fun testBookSelection() = runComposeUiTest {
        var clickedBook: BibleBook? = null
        setContent {
            BookSelectionScreen(Modifier, OrientationProvider.Orientation.Portrait) { book ->
                clickedBook = book
            }
        }

       onNodeWithText("Ps")
            .assertExists("Book not shown")
            .performClick()

        assertNotNull(clickedBook, "Book not selected")
        assertEquals(BibleBook.PS, clickedBook)
    }

    @Test
    fun testChapterSelection() = runComposeUiTest {
        var clickedChapter: Int? = null
        setContent {
            val viewModel = ChapterSelectorViewModel(
                BibleBook.PS,
                ReferenceSelectionUseCase(UsfmFileReader(), FakeCurrentReferenceRepository()),
            )

            ChapterSelectionScreen(
                BibleBook.PS,
                Modifier,
                viewModel,
                OrientationProvider.Orientation.Portrait,
            ) { ref ->
                clickedChapter = ref
            }
        }

        onNodeWithText("23")
            .assertExists("Chapter 23 does not exist")
            .performClick()

        assertNotNull(clickedChapter, "Reference not selected")
        assertEquals(23, clickedChapter)
    }
}

