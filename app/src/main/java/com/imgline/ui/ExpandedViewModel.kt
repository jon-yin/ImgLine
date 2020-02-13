package com.imgline.ui

import android.util.SparseBooleanArray
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExpandedViewModel : ViewModel(){
    val expandedArray : MutableLiveData<SparseBooleanArray> = MutableLiveData(SparseBooleanArray())
}