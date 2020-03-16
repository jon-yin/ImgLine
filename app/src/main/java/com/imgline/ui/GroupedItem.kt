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
        TODO("Not yet implemented")
    }

    override fun showError(key: String, msg: String) {
        TODO("Not yet implemented")
    }

    override fun getErrors(): Map<String, String> {
        TODO("Not yet implemented")
    }

    override fun clearErrors() {
        TODO("Not yet implemented")
    }


}