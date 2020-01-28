package com.imgline.data.imgur

import com.imgline.data.AbstractSource
import com.imgline.data.PostCallback

class ImgurSearchSource: AbstractSource() {

    companion object {
        val TAG = ImgurSearchSource::class.java
        //Endpoint https://api.imgur.com/3/gallery/search/{{sort}}/{{window}}/{{page}}?q=cats
        val ENDPOINT = "https://api.imgur.com/3/gallery/search/%s/%s/%d"
        val DEFAULT_SORT = "time"
        val DEFAULT_WINDOW = "all"

        val names = listOf<String>(
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

    private fun getEndpoint(page: Int): String {
        return String.format(ENDPOINT,
            args.get("SORT") ?: DEFAULT_SORT,
            args.get("WINDOW") ?: DEFAULT_WINDOW,
            page
            )
    }

    override fun loadImages(page: Int, callback: PostCallback) {

    }
}