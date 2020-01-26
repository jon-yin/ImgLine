package com.imgline.data

interface PostCallback {
    fun onSuccess(posts: List<Post>, page: Int)
    fun onFailure(reason: String)
}


abstract class AbstractSource {
    protected lateinit var args: Map<String, String>
    abstract val origin: String

    fun init(args: Map<String, String>) {
        this.args = args
    }

    abstract fun loadImages(page: Int, callback: PostCallback)
}



