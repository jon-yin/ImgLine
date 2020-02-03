package com.imgline.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FeedDao {

    @Query("SELECT * FROM feed")
    fun getFeedsNoSources() : LiveData<List<EntityFeed>>

    @Query("SELECT * FROM feed")
    @Transaction
    fun _feedsWithSources() : List<FeedWithSources>

    @Query("SELECT * FROM feed where id = :id")
    @Transaction
    fun _getFeedSourceWithID(id: Int) : FeedWithSources


    @Transaction
    @Delete
    fun deleteFeed(feed: EntityFeed)

    @Insert
    fun insertFeed(feed: EntityFeed): Long

    @Update
    fun updateFeed(feed: EntityFeed)

    @Insert
    fun insertSources(source: List<EntitySource>)

    @Delete
    fun deleteSource(source: EntitySource)

    @Update
    fun updateSource(source: EntitySource)

}