package com.imgline.data.imgur

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.imgline.BuildConfig
import com.imgline.data.*
import okhttp3.*
import okhttp3.MediaType
import java.io.IOException
import java.util.*

class DefaultImgurSource() : AbstractSource() {
    override val origin: String = this::class.java.name
    private val client = OkHttpClient()
    private val gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, EpochDeserializer())
        .create()
    //private val cachedData = hashMapOf<Int, List<ImgurAlbum>>()

    companion object {
        //{{section}}/{{sort}}/{{window}}/{{page}}
        val GALLERY_ENDPOINT = "https://api.imgur.com/3/gallery/%s/%s/%s/%d"
        val DEFAULT_SECTION = "hot"
        val DEFAULT_TIME = "day"
        val DEFAULT_SORT = "viral"
    }

    private fun getEndpoint(page: Int) = String.format(
        GALLERY_ENDPOINT,
        args.get("SECTION") ?: DEFAULT_SECTION,
        args.get("SORT") ?: DEFAULT_SORT,
        args.get("TIME") ?: DEFAULT_TIME,
        args.get("PAGE") ?: page
    )

    override fun loadImages(page: Int, callback: PostCallback) {
        val clientID = BuildConfig.ImgurCID
        val endpoint = getEndpoint(page)
        val request = Request.Builder()
            .addHeader("Authorization", "Client-ID $clientID")
            .url(endpoint)
            .get()
            .build()
        client.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onFailure(e.message ?: "")
                }
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body!!.string()
                    val albums = gson
                        .fromJson<JsonObject>(body, JsonObject::class.java)
                        .getAsJsonArray("data")
                        .toString()
                    val albumDataObj = gson.fromJson<List<GalleryItem>>(
                        albums, object: TypeToken<List<GalleryItem>>(){}.type
                    )
                    val posts = albumDataObj
                        .filterNot {it.isAlbum && it.images.size == 0}
                        .map{
                        if (it.isAlbum) {
                            Post(it.id, it.images[0].link, it.rating, it.isAlbum, mimeTypeToMediaType(it.images[0].type), origin)
                        } else {
                            Post(it.id, it.link, it.rating, it.isAlbum, mimeTypeToMediaType(it.type), origin)
                        }
                    }
                    callback.onSuccess(posts, page)
                }
            })

    }
}