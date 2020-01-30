package com.imgline.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface FeedDao {

    @Query("SELECT * FROM Feed")
    fun getAllFeeds(): LiveData<List<EntityFeed>>

    @Query("SELECT * FROM Feed WHERE id = :id")
    fun getFeedWithId(id: Int): LiveData<EntityFeed>

    fun insertFeed(feed: EntityFeed)

    fun deleteFeed(feed: EntityFeed)

    fun updateFeed(feed: EntityFeed)

}