package com.imgline.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imgline.R
import com.imgline.data.models.Source
import com.imgline.ui.SourceArgsViewModel
import com.imgline.ui.SourcesViewModel

class CreateFeedFragment : Fragment() {

    private lateinit var adapter: FeedSourceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_feed, container, false)
        val addIcon = view.findViewById<ImageView>(R.id.imageView)
        val addText = view.findViewById<TextView>(R.id.textView)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        addIcon.setOnClickListener {
            findNavController().navigate(R.id.action_createFeedFragment_to_sourceChooseFragment)
        }
        addText.setOnClickListener {
            findNavController().navigate(R.id.action_createFeedFragment_to_sourceChooseFragment)
        }
        Log.d(this.javaClass.simpleName, "CREATED!")
        val sourceViewModel : SourcesViewModel by navGraphViewModels(R.id.create_feed)
        Log.d(this.javaClass.simpleName, "Sources: ${sourceViewModel.sources.size}")
        val adapter = FeedSourceAdapter(sourceViewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (sourceViewModel.sources.size != 0) {
                    AlertDialog.Builder(requireActivity())
                        .setMessage(R.string.warning_back_press)
                        .setIcon(R.drawable.ic_warning_24px)
                        .setPositiveButton(R.string.yes) {
                            _, _ ->
                                findNavController().popBackStack()
                        }
                        .setNegativeButton(R.string.cancel, null).show()
                }
            }
        })
        return view
    }
}

class FeedSourceViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Source, position: Int, adapter: FeedSourceAdapter) {
        val nameView = itemView.findViewById<TextView>(R.id.source_name)
        val icon = itemView.findViewById<ImageView>(R.id.source_icon)
        val deleteIcon = itemView.findViewById<ImageView>(R.id.delete_item)
        deleteIcon.setOnClickListener{
            _ -> adapter.handleDelete(position)
        }
        nameView.text = item.name
        icon.setImageDrawable(itemView.context.getDrawable(item.origin.iconResource))
    }

}

class FeedSourceAdapter(val items : SourcesViewModel) : RecyclerView.Adapter<FeedSourceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedSourceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FeedSourceViewHolder(inflater.inflate(R.layout.recycler_view_source, parent, false))
    }

    override fun getItemCount(): Int {
        return items.sources.size
    }

    override fun onBindViewHolder(holder: FeedSourceViewHolder, position: Int) {
        holder.bind(items.sources[position], position, this)
    }

    fun handleDelete(position: Int) {
        items.sources.removeAt(position)
        notifyItemRemoved(position)
    }
}