package com.imgline.data

import java.lang.IllegalArgumentException

data class Post(val id: String, val thumbnailURL: String, val rating: Int, val isMultiple: Boolean, val type: MediaType,
                val origin: Class<out Any>) {
    val fullName: String
        get() = "$id:${origin.simpleName}"
    val metadata = hashMapOf<String, String>()

    fun addMetadata(vararg entries: Pair<String, String>) {
        if (metadata.size != 0) throw IllegalArgumentException("Metadata already attached to post!")
        metadata.putAll(entries)
    }
}

enum class MediaType{
    IMAGE, VIDEO, GIF
}