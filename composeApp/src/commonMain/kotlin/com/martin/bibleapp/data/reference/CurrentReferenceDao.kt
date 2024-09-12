package com.martin.bibleapp.data.reference

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CurrentReferenceDao {

    @Upsert
    suspend fun upsert(currentReference: CurrentReference)

    @Query("SELECT * FROM currentReference WHERE id = :id")
    suspend fun getCurrentReference(id: Int): CurrentReference?
}