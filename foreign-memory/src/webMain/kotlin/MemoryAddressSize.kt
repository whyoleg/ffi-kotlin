package dev.whyoleg.foreign.memory

public actual typealias MemoryAddressSize = Int

public actual inline fun MemoryAddressSize(value: Int): MemoryAddressSize = value
public actual inline fun MemoryAddressSize(value: Long): MemoryAddressSize = value.toInt()

@PublishedApi
internal actual const val ZeroMemoryAddressSize: MemoryAddressSize = 0
