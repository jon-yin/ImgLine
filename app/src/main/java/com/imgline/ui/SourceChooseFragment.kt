package com.imgline.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.imgline.R
import com.imgline.data.FeedViewModel

class SourceChooseFragment : Fragment(){

    companion object {
        fun newInstance() : SourceChooseFragment = SourceChooseFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycler_view, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val feedModel : FeedViewModel by viewModels()
        val items = feedModel.feedHandlers

        return view
    }
}
