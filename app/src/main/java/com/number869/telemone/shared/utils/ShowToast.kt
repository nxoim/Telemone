package com.number869.telemone.shared.utils

import android.R.attr.text
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.number869.telemone.App

fun showToast(text: String) {
	Toast.makeText(App.context, text, Toast.LENGTH_LONG).show()
}

fun showToast(@StringRes id: Int) {
    Toast.makeText(App.context, id, Toast.LENGTH_LONG).show()
}

fun showToast(block: Context.() -> String) {
    Toast.makeText(App.context, block(App.context), Toast.LENGTH_LONG).show()
}

