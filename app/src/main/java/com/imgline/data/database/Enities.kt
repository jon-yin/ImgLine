package com.imgline.data.database

import androidx.room.*
import com.imgline.ui.SpecificSourceType

@Entity(tableName = "source", foreignKeys = arrayOf(ForeignKey(
    entity = EntityFeed::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("feed_id"),
    onDelete = ForeignKey.CASCADE
        )
    ),
    indices = arrayOf(Index(value = arrayOf("id", "feed_id")))
)
data class EntitySource(@PrimaryKey(autoGenerate = true)
                        val id: Int, val name: String, val args: Map<String, String>, val origin: SpecificSourceType
                        , @ColumnInfo(name="feed_id")val feedId: Int)

@Entity(tableName = "feed",
    indices = arrayOf(Index(value = arrayOf("id"))))
data class EntityFeed(@PrimaryKey(autoGenerate = true) val id : Int, val name: String)

data class FeedWithSources(
    @Embedded
    val feed: EntityFeed,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
   val sources: List<EntitySource>
)

