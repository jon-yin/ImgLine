package com.imgline.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imgline.R
import com.imgline.data.database.EntitySource
import com.imgline.data.models.Source
import com.imgline.data.network.imgur.*
import com.imgline.ui.*

class SourceArgsFragment : Fragment() {

    companion object {

        val TYPE_ARG = "TYPE"
        val SOURCE_POSITION = "POSITION"

        fun createArguments(
            specificSourceType: SpecificSourceType,
            sourcePosition: Int
        ) : Bundle {
            return Bundle().apply {
                this.putInt(TYPE_ARG, specificSourceType.ordinal)
                this.putInt(SOURCE_POSITION, sourcePosition)
            }
        }
    }

    private val argsViewModel: SourceArgsViewModel by viewModels()
    private lateinit var args: List<Input>

    private fun getArgumentsToInflate(sourceType: SpecificSourceType) : List<Input> {
        return when (sourceType) {
            SpecificSourceType.IMGUR_FRONT_PAGE -> {listOf(
                    EditTextItem("NAME", R.string.name_field,
                        activity?.getString(R.string.imgur_default_source_name) ?:
                        SpecificSourceType.IMGUR_FRONT_PAGE.toString()),
                    SpinnerItem("SECTION",
                        R.string.section_field,
                        SECTION_OPTIONS,
                        SECTION_FRIENDLY_NAMES,
                        ImgurDefaultSource.DEFAULT_SECTION
                        ),
                    SpinnerItem("SORT",
                        R.string.sort_field,
                        SORT_OPTIONS,
                        SORT_FRIENDLY_NAMES,
                        ImgurDefaultSource.DEFAULT_SORT
                    ),
                    SpinnerItem("WINDOW",
                        R.string.window_field,
                        WINDOW_OPTIONS,
                        WINDOW_FRIENDLY_NAMES,
                        ImgurDefaultSource.DEFAULT_WINDOW
                    )
            )}
            SpecificSourceType.IMGUR_SEARCH -> {
                listOf()}
        }
    }

    private fun fetchPreviousArguments() : Map<String, String>{
        return argsViewModel.args
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_args_choose, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val sourceType = SpecificSourceType.values()[requireArguments().getInt(TYPE_ARG)]
        val sourcePosition = requireArguments().getInt(SOURCE_POSITION)
        args = getArgumentsToInflate(sourceType)
        val previousArguments = fetchPreviousArguments()
        args.forEach{
            it.inflate(requireActivity())
            it.fillFromArguments(previousArguments)
        }
        argsViewModel.errors.forEach{
            args.setError(it.value, it.key)
        }
        val adapter = ArgumentAdapter(args)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
        val button = view.findViewById<Button>(R.id.submit_button)
        val globalError = view.findViewById<TextView>(R.id.global_error)
        button.setOnClickListener{
            if (ArgsValidators.validateArgs(sourceType, args, globalError, requireActivity())) {
                val mapArgs = args.getArguments()
                val newSource = Source(mapArgs.getValue("NAME"), sourceType, mapArgs.filterKeys {it != "NAME"})
                val sourceViewModel : SourcesViewModel by navGraphViewModels(R.id.create_feed)
                sourceViewModel.sources.add(newSource)
                findNavController().navigate(R.id.action_sourceArgsFragment_to_createFeedFragment)
            }
        }
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        argsViewModel.args = args.getArguments()
        argsViewModel.errors = args.getErrors()

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
                val widget =  argument.getView()
                val name = ctx.getString(argument.friendlyKeyName)
                val textView = holder.itemView.findViewById<TextView>(R.id.widget_name)
                textView.text = name
                val constraintLayout = holder.itemView
                    .findViewById<ConstraintLayout>(R.id.constraint_layout)
                (0 until constraintLayout.childCount).map {
                    constraintLayout.getChildAt(it)
                }.filterNot{
                    it.id == textView.id
                }.forEach{
                    constraintLayout.removeView(it)
                }
                constraintLayout.addView(widget, ConstraintLayout.LayoutParams(
                    0,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    this.matchConstraintMinWidth = ConstraintLayout.LayoutParams.WRAP_CONTENT
                    this.matchConstraintPercentWidth = 0.5f
                })
                val constraintSet = ConstraintSet()
                constraintSet.clone(constraintLayout)
                constraintSet.connect(widget.id, ConstraintSet.TOP, textView.id, ConstraintSet.BOTTOM,
                    if (argument.marginTop != -1) {
                        ctx.resources.getDimensionPixelSize(argument.marginTop)
                    } else {
                        0
                    }
                )
                constraintSet.connect(widget.id, ConstraintSet.START, textView.id, ConstraintSet.START,
                    if (argument.marginStart != -1) {
                        ctx.resources.getDimensionPixelSize(argument.marginStart)
                    } else {
                        0
                    }
                )
                constraintSet.connect(widget.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,
                    if (argument.marginBottom != -1) {
                        ctx.resources.getDimensionPixelSize(argument.marginBottom)
                    } else {
                        0
                    }
                )
                constraintSet.applyTo(constraintLayout)
            }
        }

    }
}

