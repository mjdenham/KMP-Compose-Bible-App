package com.martin.bibleapp.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
fun getRoomDatabase(
    builder: RoomDatabase.Builder<BibleAppDatabase>
): BibleAppDatabase {
  return builder
      .setDriver(BundledSQLiteDriver())
      .setQueryCoroutineContext(Dispatchers.IO)
      .build()
}
// removed:
//      .addMigrations(MIGRATIONS)
//      .fallbackToDestructiveMigrationOnDowngrade()
