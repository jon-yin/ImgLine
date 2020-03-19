package com.imgline.ui.fragments.createfeed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.imgline.R
import kotlinx.android.synthetic.main.recycler_view_single_arg.view.*

class LinearGroupedItem(@StringRes title: Int?,
                        collapsible: Boolean = false,
                        vararg inputs: Input
) : GroupedItem(inputs.toList(), title) {

    private lateinit var linearLayout: LinearLayout

    override fun layoutWidgets(ctx: Context) {
        linearLayout = LinearLayout(ctx)
        linearLayout.orientation = LinearLayout.VERTICAL
        inputs.forEach{
            it.inflate(ctx)
            when (it) {
                is WidgetItem -> {
                    linearLayout.addView(inflateWidgetHolder(ctx, it, linearLayout))
                }
                is GroupedItem -> {
                    linearLayout.addView(it.getView())
                }
            }
        }
    }

    private fun inflateWidgetHolder(ctx : Context, widget: WidgetItem, linearLayout: LinearLayout) : View{
        val parentView = LayoutInflater.from(ctx).inflate(R.layout.recycler_view_single_arg,
            linearLayout,
            false
        )
        val constraintLayout = ConstraintLayout(ctx)
        val textView = parentView.widget_name
        textView.setText(ctx.getString(widget.friendlyKeyName))
        constraintLayout.addView(textView)
        constraintLayout.addView(widget.getView(),
        ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply{
            this.matchConstraintMinWidth = ConstraintLayout.LayoutParams.WRAP_CONTENT
            this.matchConstraintPercentWidth = 0.5f
        })

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(widget.getView().id, ConstraintSet.TOP, textView.id, ConstraintSet.BOTTOM,
            if (widget.marginTop != -1) {
                ctx.resources.getDimensionPixelSize(widget.marginTop)
            } else {
                0
            }
        )
        constraintSet.connect(widget.getView().id, ConstraintSet.START, textView.id, ConstraintSet.START,
            if (widget.marginStart != -1) {
                ctx.resources.getDimensionPixelSize(widget.marginStart)
            } else {
                0
            }
        )
        constraintSet.connect(widget.getView().id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,
            if (widget.marginBottom != -1) {
                ctx.resources.getDimensionPixelSize(widget.marginBottom)
            } else {
                0
            }
        )
        constraintSet.applyTo(constraintLayout)
        return constraintLayout
    }

    override fun getView(): View {
        return linearLayout
    }

    override fun fillFromArguments(args: Map<String, String>) {
        inputs.forEach{
            it.fillFromArguments(args)
        }
    }

    override fun showError(key: String, msg: String) {
        inputs.filter {
            key in it.keys
        }.forEach{
            it.showError(key, msg)
        }
    }

    override fun getErrors(): Map<String, String> {
        return inputs.map{
            it.getErrors()
        }.reduce{args1, args2 -> args1 + args2}
    }

    override fun clearErrors() {
        inputs.forEach{
            it.clearErrors()
        }
    }


}