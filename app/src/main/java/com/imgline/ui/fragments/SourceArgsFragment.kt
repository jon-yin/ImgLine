package com.imgline.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.imgline.R
import com.imgline.ui.Arguments
import com.imgline.ui.EditTextItem
import com.imgline.ui.SpecificSourceType

class SourceArgsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycler_view, container, false)

        return view
    }
}

val NAME_KEY = "name"

val SPECIFIC_SOURCES_TO_ARGS = mapOf(
    SpecificSourceType.IMGUR_SEARCH to Arguments(EditTextItem(NAME_KEY, R.string.name_field, false, null))

)
