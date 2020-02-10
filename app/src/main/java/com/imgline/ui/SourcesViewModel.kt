package com.imgline.ui;


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SourcesViewModel : ViewModel() {

    val requiredArgs = MutableLiveData<List<Input>>()

    fun updateArguments(arguments: List<Input>) {
        requiredArgs.postValue(arguments)
    }

}