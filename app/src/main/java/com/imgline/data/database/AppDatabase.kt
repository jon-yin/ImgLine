package com.imgline.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(EntitySource::class), version = 1)
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