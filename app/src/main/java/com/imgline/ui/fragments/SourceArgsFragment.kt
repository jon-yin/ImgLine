package com.imgline.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imgline.R
import com.imgline.data.network.imgur.ImgurDefaultSource
import com.imgline.ui.*

class SourceArgsFragment : Fragment() {

    companion object {

        val TYPE_ARG = "TYPE"

        fun createArguments(specificSourceType: SpecificSourceType) : Bundle {
            return Bundle().apply {
                this.putInt(TYPE_ARG, specificSourceType.ordinal)
            }
        }
    }

    private fun getArgumentsToInflate(sourceType: SpecificSourceType) : List<Input> {
        return when (sourceType) {
            SpecificSourceType.IMGUR_FRONT_PAGE -> {listOf(
                    EditTextItem("Name", R.string.name_field, false,
                        activity?.getString(R.string.imgur_default_source_name) ?:
                        SpecificSourceType.IMGUR_FRONT_PAGE.toString()),
                    SpinnerItem("SECTION", R.string.section_field, false,
                        ImgurDefaultSource.DEFAULT_SECTION,

                        ),
                    SpinnerItem("SORT", R.string.sort_field, false,
                        ImgurDefaultSource.DEFAULT_SORT),
                    SpinnerItem("WINDOW", R.string.window_field, false,
                        ImgurDefaultSource.DEFAULT_WINDOW)
            )}
            SpecificSourceType.IMGUR_SEARCH -> {
                listOf()}
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_args_choose, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val sourceType = SpecificSourceType.values()[arguments!!.getInt(TYPE_ARG)]
        val args = getArgumentsToInflate(sourceType)
        for (arg in args) {
            arg.inflate(activity!!)
        }
        val adapter = ArgumentAdapter(args)
        recyclerView.layoutManager = LinearLayoutManager(activity!!)
        recyclerView.adapter = adapter
        return view
    }
}

class ArgumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class ArgumentAdapter(val arguments: List<Input>) : RecyclerView.Adapter<ArgumentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArgumentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArgumentViewHolder(inflater.inflate(R.layout.recycler_view_single_arg, parent, false))
    }

    override fun getItemCount(): Int {
        return arguments.size
    }

    override fun onBindViewHolder(holder: ArgumentViewHolder, position: Int) {
        val argument = arguments[position]
        when (argument) {
            is WidgetItem -> {
                val ctx = holder.itemView.context
                val widget =  argument.widget
                val name = ctx.getString(argument.friendlyKeyName)
                val textView = TextView(ctx)
                if (Build.VERSION.SDK_INT >= 23) {
                    textView.setTextAppearance(R.style.listTextStyle)
                } else {
                    textView.setTextAppearance(ctx, R.style.listTextStyle)
                }
                textView.text = name
                val firstChild = holder.itemView.findViewById<FrameLayout>(R.id.first_child)
                val secondChild = holder.itemView.findViewById<FrameLayout>(R.id.second_child)
                firstChild.removeAllViews()
                secondChild.removeAllViews()
                val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT)
                params.gravity = Gravity.CENTER
                params.marginEnd = ctx.resources.getDimensionPixelSize(R.dimen.small_margin)
                val titleParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                titleParams.gravity = Gravity.CENTER_VERTICAL
                titleParams.marginStart = ctx.resources.getDimensionPixelSize(R.dimen.small_margin)
                firstChild.addView(textView, titleParams)
                secondChild.addView(widget, params)
            }
        }

    }
}

