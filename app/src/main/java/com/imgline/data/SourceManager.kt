package com.imgline.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imgline.data.imgur.DefaultImgurSource
import kotlin.reflect.KClass

class SourceManager : ViewModel(){

//    private lateinit var mSources: List<AbstractSource>
//    private var id: String = ""
    val mImages = MutableLiveData<List<Post>>()
    val source = DefaultImgurSource()

    init {
        source.init(mapOf())
    }

    companion object {
        val TAG = SourceManager::class.simpleName
    }

    fun requestImages() {
        source.loadImages(1, object: PostCallback {
            override fun onSuccess(posts: List<Post>, page: Int) {
                mImages.postValue(posts)
            }

            override fun onFailure(reason: String) {
                Log.d(TAG, reason)
            }
        })
    }

//    class SourceArg(val source : AbstractSource, val args : Map<String,String> = mapOf())
//
//    fun init(id: String, vararg sources: SourceArg) {
//        if (this.id == id) {
//            return
//        } else {
//            mSources = sources.map {
//                it.source.init(it.args)
//                it.source
//            }
//        }
//    }




}