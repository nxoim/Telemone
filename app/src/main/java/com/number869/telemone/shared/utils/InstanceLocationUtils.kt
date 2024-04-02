package com.number869.telemone.shared.utils

import com.number869.telemone.App

inline fun <reified T : Any> single(
    cacheInstance: Boolean = true,
    noinline instanceProvider: () -> T
) = App.instanceLocator.put(cacheInstance, instanceProvider)

inline fun <reified T : Any> inject() = App.instanceLocator.get<T>()