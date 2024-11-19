package com.martin.bibleapp.data.document

import kotlinx.coroutines.test.runTest
import org.crosswire.jsword.book.sword.SwordBookPath
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DocumentInstallationTest {

    private val documentInstallation = DocumentInstallation()

    @After
    fun tearDown() {
        okio.FileSystem.SYSTEM.deleteRecursively(SwordBookPath.swordBookPath)
    }

    @Test
    fun isInstalledIsInitiallyFalse() = runTest {
        assertFalse(documentInstallation.isInstalled("KingComments"))
    }

    @Test
    fun canInstallDocument() = runTest {
        documentInstallation.install("KingComments")
        assertTrue(documentInstallation.isInstalled("KingComments"))
    }
}