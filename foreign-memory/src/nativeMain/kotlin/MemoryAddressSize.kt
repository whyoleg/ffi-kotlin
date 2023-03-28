package dev.whyoleg.foreign.memory

public actual typealias MemoryAddressSize = Long

public actual inline fun MemoryAddressSize(value: Int): MemoryAddressSize = value.toLong()
public actual inline fun MemoryAddressSize(value: Long): MemoryAddressSize = value

@PublishedApi
internal actual const val ZeroMemoryAddressSize: MemoryAddressSize = 0
