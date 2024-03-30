package com.number869.telemone.data

import kotlin.reflect.KClass

class InstanceLocator {
    val instances = hashMapOf<KClass<*>, Any>()
    val lazyInstances = hashMapOf<KClass<*>, () -> Any>()

    init {
        println("InstanceLocator initialized")
    }

    inline fun <reified T : Any> putOrOverwrite(instance: T) = instances.put(T::class, instance)

    inline fun <reified T : Any> putLazy(noinline initializer: () -> T) {
        lazyInstances[T::class] = initializer
    }

    inline fun <reified T : Any> get(): T = getAndCheck(T::class)

    inline fun <reified T : Any> getLazy() = lazy { getAndCheck(T::class) }

    inline fun <reified T : Any> getOrPut(instance: T) = instances
        .getOrPut(T::class) { instance } as T

    inline fun <reified T : Any> getAndCheck(target: KClass<T>): T {
        val instance = instances[target]
            ?: lazyInstances[target]?.let { initializer ->
                val lazyInstance = initializer()
                instances[target] = lazyInstance
                lazyInstance
            }
            ?: error("${target.simpleName} not present in InstanceLocator")

        return runCatching { instance as T }.onFailure {
            // shouldnt happen
            error("Wrong data type while retrieving ${target.simpleName}. It's actually ${T::class.simpleName}'")
        }.getOrThrow()
    }

    inline fun <reified T : Any> remove() = instances.remove(T::class)
}