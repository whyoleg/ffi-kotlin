package dev.whyoleg.foreign.gradle.internal

import org.gradle.api.*
import org.gradle.api.provider.*

internal fun <T> ListProperty<T>.withAllFrom(other: ListProperty<T>?): ListProperty<T> {
    other?.let(::addAll)
    return this
}

internal inline fun <T : Any> NamedDomainObjectContainer<T>.getOrCreate(name: String, create: () -> T): T {
    val current = findByName(name)
    if (current != null) return current

    val new = create()
    add(new)
    return new
}
