package com.imgline.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(EntitySource::class, EntityFeed::class), version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun feedDao() : FeedDao

    companion object {
        private var database : AppDatabase? = null
        fun getInstance(ctx: Context): AppDatabase? {
            synchronized(AppDatabase::class) {
                if (database == null) {
                    database = Room.databaseBuilder(ctx, AppDatabase::class.java, "app_database")
                        .build()
                    return database
                } else {
                    return database
                }
            }
        }
    }
}