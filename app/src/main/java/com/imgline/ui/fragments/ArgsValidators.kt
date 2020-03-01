package com.imgline.ui.fragments

import android.content.Context
import android.util.Log
import com.imgline.R
import com.imgline.ui.*

object ArgsValidators {
    fun validateArgs(specificSourceType: SpecificSourceType,
                     inputs: List<Input>,
                     ctx: Context
                     ) : Boolean {
        inputs.clearErrors()
        when (specificSourceType) {
            SpecificSourceType.IMGUR_FRONT_PAGE -> {
                var noError = true
                val args = inputs.getArguments()
                if ((args.get("NAME") ?: "").isBlank()) {
                    inputs.setError( ctx.getString(R.string.not_empty_name), "NAME")
                    noError = false
                }
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