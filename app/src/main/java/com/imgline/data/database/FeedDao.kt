package com.imgline.data.database

import androidx.room.*

@Dao
abstract class FeedDao {

    @Query("SELECT * FROM feed")
    @Transaction
    abstract fun _feedsWithSources() : List<FeedWithSources>

    @Query("SELECT * FROM feed where id = :id")
    @Transaction
    abstract fun _getFeedSourceWithID(id: Int) : FeedWithSources

    fun getFeeds() : List<EntityFeed> {
        val feedWithSources = _feedsWithSources()
        return feedWithSources.map {
            it.feed.copy(sources=it.sources)
        }
    }

    fun getFeedWithID(id: Int) : EntityFeed {
        val feedWithSource = _getFeedSourceWithID(id)
        return feedWithSource.feed.copy(sources = feedWithSource.sources)
    }

    @Transaction
    @Delete
    abstract fun deleteFeed(feed: EntityFeed)

    @Insert
    abstract fun _insertFeed(feed: EntityFeed): Int

    @Update
    abstract fun updateFeed(feed: EntityFeed)

    @Transaction
    fun insertFeed(feed: EntityFeed) {
        val returnedId = _insertFeed(feed)
        insertSources(feed.sources.map { it.copy(feedId = returnedId) })
    }

    @Insert
    abstract fun insertSources(source: List<EntitySource>)

    @Delete
    abstract fun deleteSource(source: EntitySource)

    @Update
    abstract fun updateSource(source: EntitySource)

}