package com.imgline.ui.fragments.createfeed

import android.content.Context
import android.util.Log
import android.widget.TextView
import com.imgline.R
import com.imgline.ui.*

object ArgsValidators {
    private fun List<Input>.assertName(ctx: Context, args: Map<String, String>) : Boolean {
        if ("NAME" !in args || (args["NAME"] ?: "").isBlank()) {
            setError(ctx.getString(R.string.not_empty_name), "NAME")
            return false
        }
        return true;
    }

    fun validateArgs(specificSourceType: SpecificSourceType,
                     inputs: List<Input>,
                     globalError: TextView,
                     ctx: Context
                     ) : Boolean {
        inputs.clearErrors()
        globalError.text = null
        val args = inputs.getArguments()
        if (!inputs.assertName(ctx, args)) {
            return false
        }
        when (specificSourceType) {
            SpecificSourceType.IMGUR_FRONT_PAGE -> {
                var noError = true
                val sortArg = args.getValue("SORT")
                val sectionArg = args.getValue("SECTION")
                Log.d("VALIDATOR", sortArg)
                Log.d("VALIDATOR", sectionArg)
                if (sectionArg != "user" && sortArg == "rising") {
                    val errorMsg = ctx.getString(R.string.incompatible_values, "User Submitted", "Rising")
                    inputs.setError(errorMsg, "SORT", "SECTION")
                    noError = false
                }
                return noError
            }
            SpecificSourceType.IMGUR_SEARCH -> ""
        }
        return false
    }
}