@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign

public actual typealias MemorySizeInt = Long

public actual inline fun Int.toMemorySizeInt(): MemorySizeInt = toLong()
public actual inline fun Long.toMemorySizeInt(): MemorySizeInt = this
