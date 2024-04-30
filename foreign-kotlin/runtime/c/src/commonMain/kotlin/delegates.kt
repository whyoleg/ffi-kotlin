package dev.whyoleg.foreign.c

import kotlin.reflect.*

// TODO: decide on delegates
public inline operator fun <KT : Any> CPointer<KT>.getValue(thisRef: Any?, property: KProperty<*>): KT =
    TODO()

public inline operator fun <KT : Any> CPointer<KT>.setValue(thisRef: Any?, property: KProperty<*>, value: KT): Unit =
    TODO()
