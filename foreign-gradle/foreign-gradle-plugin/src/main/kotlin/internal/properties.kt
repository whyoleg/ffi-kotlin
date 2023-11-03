package dev.whyoleg.foreign.gradle.internal

import org.gradle.api.provider.*

internal fun <T> ListProperty<T>.withAllFrom(other: ListProperty<T>?): ListProperty<T> {
    other?.let(::addAll)
    return this
}
