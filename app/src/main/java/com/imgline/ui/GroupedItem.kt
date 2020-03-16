package com.imgline.ui

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.StringRes

class LinearGroupedItem(vararg inputs: Input,
                        val orientation: Int,
                        @StringRes title: Int
) : GroupedItem(inputs.toList(), title) {

    private lateinit var linearLayout: LinearLayout

    companion object {
        val HORIZONTAL = 1
        val VERTICAL = 2
    }

    override fun layoutWidgets(ctx: Context) {
        inputs.forEach{
            it.inflate(ctx)
        }
    }

    override fun getViewGroup(): ViewGroup {
        return linearLayout
    }

    override fun fillFromArguments(args: Map<String, String>) {
        inputs.forEach{
            it.fillFromArguments(args)
        }
    }

    override fun showError(key: String, msg: String) {
        for (input in inputs) {
            if (key in input.keys) {
                input.showError(key, msg)
                return
            }
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