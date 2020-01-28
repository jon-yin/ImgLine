package com.imgline.data.imgur

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.imgline.BuildConfig
import com.imgline.data.*
import okhttp3.*
import java.io.IOException
import java.util.*

class DefaultImgurSource : AbstractSource() {
    private val client = OkHttpClient()
    private val gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, EpochDeserializer())
        .create()

    companion object {
        //{{section}}/{{sort}}/{{window}}/{{page}}
        val GALLERY_ENDPOINT = "https://api.imgur.com/3/gallery/%s/%s/%s/%d"
        val DEFAULT_SECTION = "hot"
        val DEFAULT_WINDOW = "day"
        val DEFAULT_SORT = "viral"

        fun packageArguments(
            section : String? = null,
            sort: String? = null,
            window: String? = null
        ) : Map<String, String>{
            return hashMapOf<String, String>().apply {
                section?.let {this.put("SECTION", it)}
                sort?.let{this.put("SORT", it)}
                window?.let{this.put("WINDOW", it)}
            }
        }
    }

    private fun getEndpoint(page: Int) = String.format(
        GALLERY_ENDPOINT,
        args.get("SECTION") ?: DEFAULT_SECTION,
        args.get("SORT") ?: DEFAULT_SORT,
        args.get("WINDOW") ?: DEFAULT_WINDOW,
        page
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
                        mapGalleryItemToPost(it, origin)
                    }
                    callback.onSuccess(posts, page)
                }
            })

    }
}