package com.number869.telemone.shared.utils

import android.content.Context
import android.widget.Toast

fun showToast(text: String, context: Context = inject()) {
	Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}
