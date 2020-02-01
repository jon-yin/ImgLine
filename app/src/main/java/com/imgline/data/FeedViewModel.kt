package com.imgline.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.imgline.R
import com.imgline.data.database.AppDatabase
import com.imgline.data.database.FeedDao
import com.imgline.data.network.imgur.DefaultImgurFeed
import com.imgline.data.network.imgur.Post
import com.imgline.data.network.imgur.PostCallback
import java.util.concurrent.locks.ReentrantLock

class FeedViewModel(application: Application) : AndroidViewModel(application){

    val mImages = MutableLiveData<List<Post>>()
    val source = DefaultImgurFeed()
    val feedHandlers = listOf(
        application.getString(R.string.imgur) to application.resources.getStringArray(R.array.imgur_options)
    )
    private val appDB : AppDatabase
    private val feedDao: FeedDao

    init {
        source.init(mapOf())
        appDB = AppDatabase.getInstance(application)!!
        feedDao = appDB.feedDao()
    }

    companion object {
        val TAG = FeedViewModel::class.simpleName
    }

    fun requestImages() {
        source.loadImages(1, object:
            PostCallback {
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