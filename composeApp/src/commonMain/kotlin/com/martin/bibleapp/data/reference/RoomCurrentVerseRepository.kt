package com.martin.bibleapp.data.reference

import com.martin.bibleapp.data.database.BibleAppDatabase
import com.martin.bibleapp.domain.reference.CurrentReferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.versification.system.Versifications

class RoomCurrentVerseRepository(bibleAppDatabase: BibleAppDatabase) : CurrentReferenceRepository {
    private val currentVerseDao = bibleAppDatabase.currentReferenceDao()

    override suspend fun updateCurrentVerse(verse: Verse) {
        currentVerseDao.upsert(CurrentVerse(CURRENT_VERSE_ID, verse.book, verse.chapter, verse.verse))
    }

    override fun getCurrentReferenceFlow(): Flow<Verse> {
        return currentVerseDao.getCurrentVerse(CURRENT_VERSE_ID)
            .map { if (it != null) Verse(v11n, it.book, it.chapter, it.verse) else Verse.DEFAULT }
    }

    companion object {
        const val CURRENT_VERSE_ID = 1
        val v11n = Versifications.instance().getVersification(Versifications.DEFAULT_V11N)
    }
}