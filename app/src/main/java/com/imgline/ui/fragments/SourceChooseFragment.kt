package com.imgline.ui.fragments

import android.os.Build
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.core.view.updateMarginsRelative
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.imgline.R
import com.imgline.ui.SOURCE_TO_SPECIFIC
import com.imgline.ui.SourceType
import com.imgline.ui.SpecificSourceType

class SourceChooseFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_recycler_view, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL))
        val adapter =
            SourceAdapter(SourceType.values().toList())
        recyclerView.adapter = adapter
        return view
    }
}


class SourceChooseViewHolder(view : View, val adapter: SourceAdapter) : RecyclerView.ViewHolder(view) {

    fun bind(type: SourceType, position: Int, isExpanded: Boolean) {
        val category = itemView.findViewById<TextView>(R.id.category_text)
        val icon = itemView.findViewById<ImageView>(R.id.icon_logo)
        category.text = itemView.context.getString(type.stringResource)
        icon.setImageDrawable(itemView.context.getDrawable(type.iconResource))
        val generalItem = itemView.findViewById<LinearLayout>(R.id.general_category)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.source_types)
        linearLayout.removeAllViews()
        addChildren(linearLayout, SOURCE_TO_SPECIFIC.get(type) ?: listOf())
        linearLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
        generalItem.setOnClickListener{
            adapter.updateExpandedArray(!isExpanded, position)
            adapter.notifyItemChanged(position)
        }
    }

    fun addChildren(parent: LinearLayout, options: List<SpecificSourceType>) {
        val context = itemView.context
        val smallMargin = context.resources.getDimensionPixelSize(R.dimen.small_margin)
        val largeMargin = context.resources.getDimensionPixelSize(R.dimen.larger_icon)
        for (option in options) {
            val textView = TextView(itemView.context)
            textView.text = context.getString(option.stringResource)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(largeMargin, smallMargin, smallMargin, smallMargin)
            textView.layoutParams = params
            textView.setOnClickListener{

            }
            parent.addView(textView)
        }
    }

}

class SourceAdapter(val types : List<SourceType>) : RecyclerView.Adapter<SourceChooseViewHolder>() {

    val isExpanded = SparseBooleanArray(types.size)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceChooseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SourceChooseViewHolder(
            inflater.inflate(
                R.layout.recycler_view_list_options,
                parent,
                false
            ), this
        )
    }

    override fun getItemCount(): Int = types.size

    override fun onBindViewHolder(holder: SourceChooseViewHolder, position: Int) {
        holder.bind(types[position], position, isExpanded[position, false])
    }

    fun updateExpandedArray(newState: Boolean, position: Int) {
        isExpanded.put(position, newState)
    }
}