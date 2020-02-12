package com.imgline.data.network

import com.imgline.ui.HasSourceOrigin


abstract class AbstractSource(val args: Map<String, String>) : HasSourceOrigin {

    abstract fun loadImages(page: Int, callback: PostCallback)
}



