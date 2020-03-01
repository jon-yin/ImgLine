package com.imgline.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SourceArgsViewModel : ViewModel() {

    var args = mapOf<String, String>()
    var errors = mapOf<String, String>()

}