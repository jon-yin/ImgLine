package com.imgline.ui

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import com.imgline.R


sealed class Input {
    abstract val type: Int
    abstract fun inflate(ctx : Context)
    abstract fun getArguments(): Map<String, String>
    abstract fun fillFromArguments(args: Map<String, String>)
}

abstract class WidgetItem(val key: String, @StringRes val friendlyKeyName: Int, val isRequired: Boolean, val default: String?): Input() {

    override val type: Int = 1
    abstract val widget : View

    override fun getArguments(): Map<String, String> {
        val value = getValue()
        return when {
            value != null -> {mapOf(key to value)}
            default == null -> {if (isRequired) {
                throw IllegalArgumentException("Argument specified as required but nothing supplied")
                } else{
                    return mapOf()
                }
            }
            else -> mapOf(key to default)
        }
    }

    abstract fun getValue() : String?
    abstract fun showError(error: String)

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
        widget.isFocusableInTouchMode = true
        widget.id = View.generateViewId()
    }

    override fun getValue(): String? {
        return widget.getValue()
    }

    override fun fillFromArguments(args: Map<String, String>) {
        val toFill = args.get(key) ?: default
        if (toFill != null) {
            widget.setSelectedValue(toFill)
        }
    }

    override fun showError(error: String) {
        widget.error = error
    }
}

class EditTextItem(key: String, friendlyKeyName: Int, isRequired: Boolean, default: String? = null)
    : WidgetItem(key, friendlyKeyName, isRequired, default) {

    override lateinit var widget: EditText

    override fun inflate(ctx: Context) {
        widget = EditText(ctx)
        widget.id = View.generateViewId()
    }

    override fun getValue(): String? {
        widget.text.trim().let{if (it.isBlank()) {return null} else {return it.toString()} }
    }

    override fun fillFromArguments(args: Map<String, String>) {
        args.get(key)?.let{widget.setText(it)}
        if (widget.text.isBlank()) {
            widget.setText(default ?: widget.text)
        }
    }

    override fun showError(error: String) {
        widget.error = error
    }
}

class CustomSpinnerTextView(ctx: Context,
                            private val values: Array<String>,
                            private val friendlyNames: Array<String>,
                            defaultValue : String?) : TextView(ctx) {
    private var currentSelectedItem : Int = defaultValue?.let{values.indexOf(it)} ?: 0
    val menu = PopupMenu(ctx, this, Gravity.END)
    val menuItems : List<Int> = List(values.size) {
        View.generateViewId()
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

    fun setSelectedValue(newValue: String){
        values.indexOf(newValue)
            .also { if (it != -1) currentSelectedItem = it }
        text = friendlyNames[currentSelectedItem]
    }

    fun getValue() : String {
        return values[currentSelectedItem]
    }
}

interface Validator {
    fun isValid() : Pair<Boolean, Int>
}

class ExcludeArgumentsValidator(val unallowedArg: Pair<String, String?>, val unallowedArg2: Pair<String, String?>): Validator {

    override fun isValid(): Pair<Boolean, Int> {
        return true to 0
    }
}