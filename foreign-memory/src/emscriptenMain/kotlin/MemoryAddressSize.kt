package dev.whyoleg.foreign.memory

public actual typealias MemoryAddressSize = Int

public actual inline fun memoryAddressSize(value: Int): MemoryAddressSize = value
public actual inline fun memoryAddressSize(value: Long): MemoryAddressSize = value.toInt()
public actual inline fun memoryAddressSizeZero(): MemoryAddressSize = 0
