package com.imgline.data.network.imgur

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.imgline.data.EpochDeserializer
import okhttp3.*
import java.io.IOException
import java.util.*

abstract class AbstractImgurFeed : AbstractFeed() {
    protected val client = OkHttpClient()
    protected val gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, EpochDeserializer())
        .create()

    protected abstract fun getEndpoint(page: Int): String

    protected abstract fun parsePosts(jsonString: String): List<Post>

    protected abstract fun buildRequest(url: String) : Request

    override fun loadImages(page: Int, callback: PostCallback) {
        val endpoint = getEndpoint(page)
        val request = buildRequest(endpoint)
        client.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onFailure(e.message ?: "")
                }
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body!!.string()
                    Log.d(origin.simpleName, body)
                    callback.onSuccess(parsePosts(body), page)
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
                            mapGalleryItemToPost(it, origin)
                        }
                    callback.onSuccess(posts, page)
                }
            })
    }

}