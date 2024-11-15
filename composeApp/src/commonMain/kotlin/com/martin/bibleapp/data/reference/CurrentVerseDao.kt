package com.martin.bibleapp.data.reference

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentVerseDao {

    @Upsert
    suspend fun upsert(currentVerse: CurrentVerse)

    @Query("SELECT * FROM currentVerse WHERE id = :id")
    fun getCurrentVerse(id: Int): Flow<CurrentVerse?>
}