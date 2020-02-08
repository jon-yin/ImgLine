package com.imgline.ui

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes
import com.imgline.R

class Arguments(vararg inputs: Input) {
    val args : List<Input> = inputs.toList()

}


abstract class Input {
    abstract fun inflate(ctx : Context)
    abstract fun addArguments(args: MutableMap<String, String>)
    //protected abstract fun getArgument() : Pair<String, String>?

}

abstract class WidgetItem(val key: String, @StringRes val friendlyKeyName: Int, val isRequired: Boolean, val default: String?): Input() {

    abstract val widget : View

    override fun addArguments(args: MutableMap<String, String>) {
        val value = getValue()
        when  {
            value != null -> {args += key to value}
            default == null -> {if (isRequired)
                throw IllegalArgumentException("Argument specified as required but nothing supplied")}
            else -> {args += key to default}
        }
    }

    abstract fun getValue() : String?
}

class SpinnerItem(key: String, friendlyKeyName: Int, isRequired: Boolean, default: String? = null)
    : WidgetItem(key, friendlyKeyName, isRequired, default) {

    override lateinit var widget: CustomSpinnerTextView

    override fun inflate(ctx: Context) {
        widget = CustomSpinnerTextView(ctx)
    }

    override fun getValue(): String? {
        return widget.text.toString()
    }
}

class EditTextItem(key: String, friendlyKeyName: Int, isRequired: Boolean, default: String? = null)
    : WidgetItem(key, friendlyKeyName, isRequired, default) {

    override lateinit var widget: EditText

    override fun inflate(ctx: Context) {
        widget = EditText(ctx)
    }

    override fun getValue(): String? {
        return widget.text.trim().toString()
    }
}

class CustomSpinnerTextView(ctx: Context) : TextView(ctx) {
    init {
        setBackgroundResource(R.drawable.box_border)
    }
}

