package com.imgline.data.network.imgur

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.imgline.BuildConfig
import okhttp3.Request

class ImgurSearchFeed: AbstractImgurFeed() {

    companion object {
        val TAG = ImgurSearchFeed::class.java
        //Endpoint https://api.imgur.com/3/gallery/search/{{sort}}/{{window}}/{{page}}?q=cats
        val ENDPOINT = "https://api.imgur.com/3/gallery/search/%s/%s/%d"
        val DEFAULT_SORT = "time"
        val DEFAULT_WINDOW = "all"

        val names = listOf(
            "SORT",
            "WINDOW",
            "Q",
            "QANY",
            "QEXACTLY",
            "QNOT",
            "QTYPE",
            "QSIZE"
        )

        fun packageArguments(
            sort: String? = null,
            window: String? = null,
            q: String? = null,
            qAny: String? = null,
            qExactly: String? = null,
            qNot: String? = null,
            qType: String? = null,
            qSize: String? = null
        ) : Map<String, String> {
            val zipped = names.zip(listOf(sort, window, qAny, qExactly, qNot, qType, qSize))
            return zipped.filter {it.second != null}.map {it.first to it.second!!}.toMap()
        }

    }

    override fun getEndpoint(page: Int): String {
        return String.format(ENDPOINT,
            args.get("SORT") ?: DEFAULT_SORT,
            args.get("WINDOW") ?: DEFAULT_WINDOW,
            page
            )
    }

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
        return Request.Builder()
            .get()
            .url(url)
            .addHeader("Authorization", "${BuildConfig.ImgurCID}")
            .build()
    }
}