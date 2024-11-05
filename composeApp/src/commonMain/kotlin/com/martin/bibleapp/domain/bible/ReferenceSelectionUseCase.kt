package com.martin.bibleapp.domain.bible

import org.crosswire.jsword.versification.BibleBook
import com.martin.bibleapp.domain.reference.CurrentReferenceRepository
import com.martin.bibleapp.domain.reference.Reference

class ReferenceSelectionUseCase(private val reader: BibleReader, private val currentReferenceRepository: CurrentReferenceRepository) {

    suspend fun getNumChapters(book: BibleBook): Int = reader.countChapters(book)

    suspend fun selectReference(reference: Reference) = currentReferenceRepository.updateCurrentReference(reference)
}