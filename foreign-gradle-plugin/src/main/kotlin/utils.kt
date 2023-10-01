package dev.whyoleg.foreign.gradle

import org.gradle.api.provider.*

internal fun <T> ListProperty<T>.withAllFrom(other: ListProperty<T>?): ListProperty<T> {
    other?.let(::addAll)
    return this
}
