package com.number869.telemone.shared.utils

import android.content.Context
import android.widget.Toast
import com.number869.telemone.App

fun showToast(text: String) {
	Toast.makeText(App.context, text, Toast.LENGTH_LONG).show()
}
