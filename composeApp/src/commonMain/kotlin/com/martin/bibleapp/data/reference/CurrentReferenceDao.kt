package com.martin.bibleapp.data.reference

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentReferenceDao {

    @Upsert
    suspend fun upsert(currentReference: CurrentReference)

    @Query("SELECT * FROM currentReference WHERE id = :id")
    fun getCurrentReference(id: Int): Flow<CurrentReference?>
}