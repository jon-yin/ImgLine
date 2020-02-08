package com.imgline.ui;


import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.imgline.data.database.EntitySource
import com.imgline.data.network.imgur.AbstractSource

class SourcesViewModel : ViewModel() {

    val requiredArgs = MutableLiveData<Arguments>()

    fun updateArguments(arguments: Arguments) {
        requiredArgs.postValue(arguments)
    }

}