package com.imgline.data.database

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.imgline.data.network.imgur.AbstractFeed


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
    fun clazz2String(clazz: Class<out AbstractFeed>) = clazz.name

    @TypeConverter
    fun String2clazz(className: String) = Class.forName(className)

}