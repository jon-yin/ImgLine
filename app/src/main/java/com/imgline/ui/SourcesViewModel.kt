package com.imgline.ui;


import androidx.lifecycle.ViewModel;
import com.imgline.data.database.EntitySource
import com.imgline.data.network.imgur.AbstractSource

class SourcesViewModel : ViewModel() {

    val sources = arrayListOf<EntitySource>()

    fun addSource(name: String, args: Map<String, String>, clazz: Class<out AbstractSource>) {
        val source = EntitySource(0, name, args, clazz.name, 0)
        sources.add(source)
    }
}