package com.number869.telemone.shared.utils

import com.number869.telemone.App

inline fun <reified T : Any> inject() = App.instanceLocator.get<T>()