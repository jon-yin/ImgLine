package com.imgline.ui.fragments.createfeed

import android.content.Context
import android.view.View
import androidx.annotation.StringRes

val List<Input>.name : String?
    get() = getArguments()["NAME"]

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
    abstract fun getErrors() : Map<String, String>
    abstract fun clearErrors()
    abstract fun getView() : View
}

val WIDGET_ITEM = 1
val GROUPED_ITEM = 2

abstract class WidgetItem(val key: String, @StringRes val friendlyKeyName: Int, val default: String?): Input() {

    override val keys: List<String> = listOf(key)
    override val type = WIDGET_ITEM
    abstract val widget : View
    var marginStart = -1
    var marginTop = -1
    var marginBottom = -1

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

abstract class GroupedItem(val inputs: List<Input>, @StringRes val title : Int?) : Input() {
    override val keys: List<String> = inputs.getArguments().keys.toList()
    override val type: Int = GROUPED_ITEM

    override fun getArguments(): Map<String, String> {
        return inputs.getArguments()
    }

    abstract fun layoutWidgets(ctx: Context)

    final override fun inflate(ctx: Context) {
        layoutWidgets(ctx)
    }

}