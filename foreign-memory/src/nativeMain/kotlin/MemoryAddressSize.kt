package dev.whyoleg.foreign.memory

public actual typealias MemoryAddressSize = Long

public actual inline fun memoryAddressSize(value: Int): MemoryAddressSize = value.toLong()
public actual inline fun memoryAddressSize(value: Long): MemoryAddressSize = value
public actual inline fun memoryAddressSizeZero(): MemoryAddressSize = 0
