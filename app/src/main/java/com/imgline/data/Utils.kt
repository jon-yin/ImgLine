package com.imgline.data

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.imgline.data.network.imgur.MediaType
import java.lang.reflect.Type
import java.util.*

class EpochDeserializer: JsonDeserializer<Date> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date = if (json != null) { Date(json.asLong) } else { Date() }

}

fun mimeTypeToMediaType(type: String) : MediaType = when(type.trim()) {
            "image/gif" -> MediaType.GIF
            "image/jpg", "image/jpeg", "image/png" -> MediaType.IMAGE
            "video/mp4" -> MediaType.VIDEO
            else -> MediaType.IMAGE
        }

