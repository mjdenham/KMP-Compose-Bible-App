package com.martin.bibleapp.domain.reference

import kotlinx.coroutines.flow.Flow
import org.crosswire.jsword.passage.Verse

interface CurrentReferenceRepository {
    suspend fun updateCurrentVerse(verse: Verse)
    fun getCurrentReferenceFlow(): Flow<Verse>
}