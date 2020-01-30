package com.imgline.data.network.imgur


abstract class AbstractFeed {
    protected lateinit var args: Map<String, String>
    val origin
        get() = this::class.java

    fun init(args: Map<String, String>) {
        this.args = args
    }

    abstract fun loadImages(page: Int, callback: PostCallback)
}



