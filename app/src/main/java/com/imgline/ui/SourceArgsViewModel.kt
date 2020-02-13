package com.imgline.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SourceArgsViewModel :ViewModel() {

    val args = MutableLiveData<Map<String, String>>(mapOf())

}