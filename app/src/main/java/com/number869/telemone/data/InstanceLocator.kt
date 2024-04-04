package com.number869.telemone.data

import kotlin.reflect.KClass

class InstanceLocator {
    val instanceReferences = hashMapOf<KClass<*>, () -> Any>()
    val instanceCache = hashMapOf<KClass<*>, Any>()
    val instancesToCache = hashSetOf<KClass<*>>()

    init {
        println("InstanceLocator initialized")
    }

    inline fun <reified T : Any> put(
        cacheInstance: Boolean = true,
        createEagerly: Boolean = false,
        crossinline instanceProvider: () -> T
    ) {
        if (cacheInstance)
            instancesToCache.add(T::class)
        else
            instancesToCache.remove(T::class)

        instanceReferences[T::class] = { instanceProvider() }
        if (createEagerly) get<T>()
    }

    inline fun <reified T : Any> get(): T {
        val cachedInstance = instanceCache[T::class]

        val instanceProvider = instanceReferences[T::class]
            ?: error("${T::class.simpleName} not present in InstanceLocator")

        val instance = cachedInstance ?: instanceProvider()
        if (instancesToCache.contains(T::class)) instanceCache[T::class] = instance

        return runCatching { instance as T }.onFailure {
            // shouldn't happen
            error("Wrong data type while retrieving ${T::class.simpleName}. It's actually ${instance::class.simpleName}")
        }.getOrThrow()
    }

    inline fun <reified T : Any> remove() {
        instanceReferences.remove(T::class)
        instanceCache.remove(T::class)
    }
}