package com.imgline.data.database

import androidx.room.*
import com.imgline.data.network.imgur.AbstractFeed

@Entity(tableName = "source", foreignKeys = arrayOf(ForeignKey(
    entity = EntityFeed::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("feed_id"),
    onDelete = ForeignKey.CASCADE
        )
    )
)
data class EntitySource(@PrimaryKey(autoGenerate = true)
                        val id: Int, val name: String, val args: Map<String, String>, val origin: Class<out AbstractFeed>
                        , @ColumnInfo(name="feed_id")val feedId: Int)

@Entity(tableName = "feed")
data class EntityFeed(@PrimaryKey(autoGenerate = true) val id : Int, val name: String, @Ignore val sources: List<EntitySource>)

data class FeedWithSources(
    @Embedded
    val feed: EntityFeed,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
   val sources: List<EntitySource>
)

