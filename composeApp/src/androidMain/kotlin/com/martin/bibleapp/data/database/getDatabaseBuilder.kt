package com.martin.bibleapp.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<BibleAppDatabase> {
  val appContext = ctx.applicationContext
  val dbFile = appContext.getDatabasePath("bibleApp.db")
  return Room.databaseBuilder<BibleAppDatabase>(
    context = appContext,
    name = dbFile.absolutePath
  )
}