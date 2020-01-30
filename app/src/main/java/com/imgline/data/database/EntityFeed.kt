package com.imgline.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.imgline.data.network.imgur.AbstractFeed

@Entity(tableName = "Feed")
data class EntityFeed(@PrimaryKey(autoGenerate = true) val id: Int, val name: String, val args: Map<String, String>, val origin: Class<out AbstractFeed>)