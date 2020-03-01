package com.imgline.ui

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.imgline.R


fun List<Input>.getArguments() : Map<String, String> {
    return this
        .map { it.getArguments() }
        .reduce { input1, input2 -> input1 + input2 }
}

fun List<Input>.getInputWithKey(key: String): Input {
    return this.filter {
        key in it.keys
    }.firstOrNull() ?: throw IllegalArgumentException("No element with specified key found")
}

fun List<Input>.getErrors() : Map<String, String> {
    return this
        .map { it.getErrors() }
        .reduce { input1, input2 -> input1 + input2 }
}

fun List<Input>.setError(message: String, vararg keys: String) {
    for (key in keys) {
        getInputWithKey(key).showError(key, message)
    }
}

fun List<Input>.clearErrors() {
    this.forEach{
        it.clearErrors()
    }
}

sealed class Input {
    abstract val type: Int
    abstract val keys: List<String>
    abstract fun inflate(ctx : Context)
    abstract fun getArguments(): Map<String, String>
    abstract fun fillFromArguments(args: Map<String, String>)
    abstract fun showError(key: String, msg: String)
    fun showErrors(vararg msgs : Pair<String, String>) {
        msgs.forEach {
            val (key, value) = it
            showError(key, value)
        }
    }
    abstract fun getErrors() : Map<String, String>
    abstract fun clearErrors()
}

abstract class WidgetItem(val key: String, @StringRes val friendlyKeyName: Int, val default: String?): Input() {

    override val keys: List<String> = listOf(key)
    override val type = 1
    abstract val widget : View
    var marginStart = -1
    var marginTop = -1
    var marginBottom = -1

    abstract fun getView() : View

    override fun getArguments(): Map<String, String> {
        val value = getValue()
        return when {
            hasValue() -> {mapOf(key to value)}
            default != null -> mapOf(key to default)
            else -> mapOf()
        }
    }

    abstract fun hasValue() : Boolean
    abstract fun getValue() : String

}

class SpinnerItem(key: String, friendlyKeyName: Int,
                  val values : Array<String>,
                  val friendlyValues: Array<String>,
                  default: String? = null
                  )
    : WidgetItem(key, friendlyKeyName, default) {

    constructor(key: String,
                friendlyKeyName: Int,
                @ArrayRes values: Int,
                @ArrayRes friendlyValues: Int,
                ctx: Context,
                default: String? = null
    ) : this(key,
        friendlyKeyName,
        ctx.resources.getStringArray(values),
        ctx.resources.getStringArray(friendlyValues),
        default
    )

    override lateinit var widget: CustomSpinnerTextView
    lateinit var errorText: TextView
    lateinit var widgetLayout : LinearLayout

    init {
        marginTop = R.dimen.small_margin
        marginBottom = R.dimen.small_margin
    }

    override fun inflate(ctx: Context) {
        widgetLayout = LinearLayout(ctx)
        widgetLayout.id = View.generateViewId()
        widgetLayout.orientation = LinearLayout.VERTICAL
        widget = CustomSpinnerTextView(ctx, values, friendlyValues, default)
        //widget.id = View.generateViewId()
        errorText = TextView(ctx)
        if (Build.VERSION.SDK_INT >= 23) {
            errorText.setTextAppearance(R.style.TextAppearance_Design_Error)
        } else {
            errorText.setTextAppearance(ctx, R.style.TextAppearance_Design_Error)
        }
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
            )
        widgetLayout.addView(widget, params)
        widgetLayout.addView(errorText, params)
    }

    override fun getValue(): String {
        return widget.getValue()
    }

    override fun hasValue(): Boolean {
        return true
    }

    override fun fillFromArguments(args: Map<String, String>) {
        val toFill = args.get(key) ?: default
        if (toFill != null) {
            widget.setSelectedValue(toFill)
        }
    }

    override fun getView(): View {
        return widgetLayout
    }

    override fun showError(key: String, msg: String) {
        errorText.text = msg
        widget.hasError = true
        widget.refreshDrawableState()
    }

    override fun clearErrors() {
        widget.background = widget.context.getDrawable(R.drawable.box_border)
        widget.hasError = false
        errorText.text = null
        widgetLayout.refreshDrawableState()
    }

    override fun getErrors(): Map<String, String> {
        return if (widget.hasError) {
            mapOf(key to errorText.text.toString())
        } else {
            mapOf()
        }
    }
}

class EditTextItem(key: String, friendlyKeyName: Int, val initialText : String, default: String? = null)
    : WidgetItem(key, friendlyKeyName, default) {

    override lateinit var widget: TextInputEditText
    lateinit var widgetLayout: TextInputLayout

    override fun inflate(ctx: Context) {
        widgetLayout = TextInputLayout(ctx)
        widgetLayout.id = View.generateViewId()
        widgetLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
        widget = TextInputEditText(ctx)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
            this.marginStart = ctx.resources.getDimensionPixelSize(R.dimen.negative_offset)
        }
        widget.setText(initialText)
        widgetLayout.addView(widget, params)
        widgetLayout.isErrorEnabled = true
    }

    override fun hasValue(): Boolean {
        return widget.text?.isNotEmpty() ?: false
    }

    override fun getValue(): String {
        return widget.text.toString()
    }

    override fun fillFromArguments(args: Map<String, String>) {
        args.get(key)?.let{widget.setText(it)}
    }

    override fun showError(key : String, error: String) {
        widgetLayout.isErrorEnabled = true
        widgetLayout.error = error
    }

    override fun clearErrors() {
        widgetLayout.error = null
        widgetLayout.isErrorEnabled = false
    }

    override fun getView(): View {
        return widgetLayout
    }

    override fun getErrors(): Map<String, String> {
        widgetLayout.error?.let { return mapOf(key to it.toString()) }
        return mapOf()
    }
}

class CustomSpinnerTextView(ctx: Context,
                            private val values: Array<String>,
                            private val friendlyNames: Array<String>,
                            defaultValue : String?) : TextView(ctx) {
    private var currentSelectedItem : Int = defaultValue?.let{values.indexOf(it)} ?: 0
    var hasError : Boolean = false
    val menu = PopupMenu(ctx, this, Gravity.END)
    val menuItems : List<Int> = List(values.size) {
        View.generateViewId()
    }

    companion object {
        val ERROR_STATE = intArrayOf(R.attr.state_error)
    }

    init {
        gravity = Gravity.END
        setPaddingRelative(0,0,ctx.resources.getDimensionPixelSize(R.dimen.small_margin), 0)
        setBackgroundResource(R.drawable.box_border)
        setTextSize(ctx.resources.getInteger(R.integer.spinner_item_size).toFloat())
        for ((name, id) in friendlyNames.zip(menuItems)) {
            menu.menu.add(Menu.NONE, id, Menu.NONE, name)
        }
        setOnClickListener{
            menu.show()
        }
        menu.setOnMenuItemClickListener {
            val newSelectedItem = menuItems.indexOf(it.itemId)
            if (newSelectedItem != -1) {
                currentSelectedItem = newSelectedItem
                text = friendlyNames[currentSelectedItem]
                true
            } else {
                false
            }
        }
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val states = super.onCreateDrawableState(extraSpace + 1)
        return if (hasError) {
            View.mergeDrawableStates(states, ERROR_STATE)
        } else {
            states
        }
    }

    fun setSelectedValue(newValue: String){
        values.indexOf(newValue)
            .also { if (it != -1) currentSelectedItem = it }
        text = friendlyNames[currentSelectedItem]
    }

    fun getValue() : String {
        return values[currentSelectedItem]
    }

}