package com.martin.bibleapp.fakes

import com.martin.bibleapp.domain.reference.CurrentReferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.crosswire.jsword.passage.Verse

class FakeCurrentReferenceRepository: CurrentReferenceRepository {
    private var currentVerse: Verse = Verse.DEFAULT

    override suspend fun updateCurrentVerse(verse: Verse) {
        currentVerse = verse
    }

    override fun getCurrentReferenceFlow(): Flow<Verse> {
        return flowOf(currentVerse)
    }
}