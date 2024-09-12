package com.martin.bibleapp.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

fun getDatabaseBuilder(): RoomDatabase.Builder<BibleAppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/bibleApp.db"
    return Room.databaseBuilder<BibleAppDatabase>(
        name = dbFilePath,
    )
}
