package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.domain.reference.CurrentReferenceRepository
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.versification.BibleBook

class ReferenceSelectionUseCase(private val reader: BibleReader, private val currentReferenceRepository: CurrentReferenceRepository) {

    suspend fun getNumChapters(book: BibleBook): Int = reader.countChapters(book)

    suspend fun selectVerse(verse: Verse) = currentReferenceRepository.updateCurrentVerse(verse)
}