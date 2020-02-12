package com.imgline.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imgline.data.database.AppDatabase
import com.imgline.data.database.EntityFeed
import com.imgline.data.database.FeedDao
import com.imgline.data.network.imgur.ImgurDefaultSource
import com.imgline.data.network.Post
import com.imgline.data.network.PostCallback

class FeedViewModel(application: Application) : AndroidViewModel(application){

    val mImages = MutableLiveData<List<Post>>()
    val source = ImgurDefaultSource(mapOf())

    private val appDB : AppDatabase
    private val feedDao: FeedDao

    init {
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

    fun getFeeds() : LiveData<List<EntityFeed>> = feedDao.getFeedsNoSources()



}