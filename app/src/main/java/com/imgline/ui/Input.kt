package com.imgline.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import com.imgline.R


sealed class Input {
    abstract val type: Int
    abstract fun inflate(ctx : Context)
    abstract fun addArguments(args: Map<String, String>): Map<String, String>
    abstract fun fillFromArguments(args: Map<String, String>)

    //protected abstract fun getArgument() : Pair<String, String>?

}

abstract class WidgetItem(val key: String, @StringRes val friendlyKeyName: Int, val isRequired: Boolean, val default: String?): Input() {

    override val type: Int = 1
    abstract val widget : View

    override fun addArguments(args: Map<String, String>): Map<String, String> {
        val value = getValue()
        return when  {
            value != null -> {args + (key to value)}
            default == null -> {if (isRequired) {
                throw IllegalArgumentException("Argument specified as required but nothing supplied")
                } else{
                    return args
                }
            }
            else -> {args + (key to default)}
        }
    }

    abstract fun getValue() : String?

}

class SpinnerItem(key: String, friendlyKeyName: Int,
                  isRequired: Boolean,
                  val values : Array<String>,
                  val friendlyValues: Array<String>,
                  default: String? = null
                  )
    : WidgetItem(key, friendlyKeyName, isRequired, default) {

    constructor(key: String,
                friendlyKeyName: Int,
                isRequired: Boolean,
                @ArrayRes values: Int,
                @ArrayRes friendlyValues: Int,
                ctx: Context,
                default: String? = null
    ) : this(key,
        friendlyKeyName,
        isRequired,
        ctx.resources.getStringArray(values),
        ctx.resources.getStringArray(friendlyValues),
        default
    )

    override lateinit var widget: CustomSpinnerTextView

    override fun inflate(ctx: Context) {
        widget = CustomSpinnerTextView(ctx, values, friendlyValues, default)
    }

    override fun getValue(): String? {
        return widget.text.toString()
    }

    override fun fillFromArguments(args: Map<String, String>) {
        val toFill = args.get(key) ?: default
        if (toFill != null) {
            widget.text = toFill
        }
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

    override fun fillFromArguments(args: Map<String, String>) {
        val toFill = args.get(key) ?: default
        if (toFill != null) {
            widget.setText(toFill)
        }
    }
}

class CustomSpinnerTextView(ctx: Context, private val values: Array<String>,
                            private val friendlyNames: Array<String>,
                            defaultValue : String?) : TextView(ctx) {
    private var currentSelectedItem : Int = defaultValue?.let{values.indexOf(it)} ?: 0

    init {
        gravity = Gravity.END
        setPaddingRelative(0,0,ctx.resources.getDimensionPixelSize(R.dimen.small_margin), 0)
        setBackgroundResource(R.drawable.box_border)
        setTextSize(ctx.resources.getInteger(R.integer.spinner_item_size).toFloat())
    }

    fun setSelectedValue(newValue: String){
        values.indexOf(newValue)
            .also { if (it != -1) currentSelectedItem = it }
    }
}

