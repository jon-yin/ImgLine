package com.imgline.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.imgline.R

class CreateFeedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_feed, container, false)
        val addIcon = view.findViewById<ImageView>(R.id.imageView)
        val addText = view.findViewById<TextView>(R.id.textView)
        addIcon.setOnClickListener {
            findNavController().navigate(R.id.action_createFeedFragment_to_sourceChooseFragment)
        }
        addText.setOnClickListener {
            findNavController().navigate(R.id.action_createFeedFragment_to_sourceChooseFragment)
        }
        return view
    }
}