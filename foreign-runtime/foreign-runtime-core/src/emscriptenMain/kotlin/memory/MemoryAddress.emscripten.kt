package dev.whyoleg.foreign.memory

@ForeignMemoryApi
public actual typealias MemoryAddressSize = Int

@ForeignMemoryApi
public actual inline fun memoryAddressSize(value: Int): MemoryAddressSize = value

@ForeignMemoryApi
public actual inline fun memoryAddressSize(value: Long): MemoryAddressSize = value.toInt()

@ForeignMemoryApi
public actual inline fun memoryAddressSizeZero(): MemoryAddressSize = 0
