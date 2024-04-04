package com.number869.telemone.shared.utils

import com.number869.telemone.App

inline fun <reified T : Any> single(
    cacheInstance: Boolean = true,
    createEagerly: Boolean = false,
    crossinline instanceProvider: () -> T
) = App.instanceLocator.put(cacheInstance, createEagerly, instanceProvider)

inline fun <reified T : Any> inject() = App.instanceLocator.get<T>()