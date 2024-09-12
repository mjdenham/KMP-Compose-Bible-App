package com.martin.bibleapp.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.martin.bibleapp.data.reference.CurrentReference
import com.martin.bibleapp.data.reference.CurrentReferenceDao

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object BibleAppDatabaseConstructor : RoomDatabaseConstructor<BibleAppDatabase>

@Database(entities = [CurrentReference::class], version = 1)
@ConstructedBy(BibleAppDatabaseConstructor::class)
abstract class BibleAppDatabase: RoomDatabase() {

    abstract fun currentReferenceDao(): CurrentReferenceDao

}