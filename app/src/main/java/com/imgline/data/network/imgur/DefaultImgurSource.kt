package com.imgline.data.network.imgur

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.imgline.BuildConfig
import okhttp3.Request

class DefaultImgurSource : AbstractImgurSource() {

    companion object {
        //{{section}}/{{sort}}/{{window}}/{{page}}
        val GALLERY_ENDPOINT = "https://api.imgur.com/3/gallery/%s/%s/%s/%d"
        val DEFAULT_SECTION = "hot"
        val DEFAULT_WINDOW = "day"
        val DEFAULT_SORT = "viral"
        val names = listOf(
            "SECTION",
            "SORT",
            "WINDOW"
        )

        fun packageArguments(
            section : String? = null,
            sort: String? = null,
            window: String? = null
        ) : Map<String, String>{
            return names.zip(listOf(section, sort, window)).filter { it.second != null }.map { it.first to it.second!! }.toMap()
        }
    }

    override fun getEndpoint(page: Int) = String.format(
        GALLERY_ENDPOINT,
        args.get("SECTION") ?: DEFAULT_SECTION,
        args.get("SORT") ?: DEFAULT_SORT,
        args.get("WINDOW") ?: DEFAULT_WINDOW,
        page
    )

    override fun parsePosts(jsonString: String): List<Post> {
        val albums = gson
            .fromJson<JsonObject>(jsonString, JsonObject::class.java)
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
        return posts
    }

    override fun buildRequest(url: String): Request {
        return Request
            .Builder()
            .addHeader("Authorization", "Client-ID ${BuildConfig.ImgurCID}")
            .url(url)
            .get()
            .build()
    }

}