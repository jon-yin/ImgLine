package com.imgline.data.database

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.imgline.ui.SpecificSourceType


class Converters {
    private val gson = GsonBuilder().create()

    @TypeConverter
    fun map2Json(map : Map<String, String>) : String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun json2Map(json: String) : Map<String, String> {
        return gson.fromJson(json, object: TypeToken<Map<String, String>>(){}.type)
    }

    @TypeConverter
    fun sourceType2String(sourceType: SpecificSourceType) : String {
        return sourceType.toString()
    }

    @TypeConverter
    fun string2SourceType(type: String) : SpecificSourceType {
        return SpecificSourceType.valueOf(type)
    }

}