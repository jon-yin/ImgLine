package com.imgline.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(EntitySource::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun feedDao() : FeedDao
}