package dev.whyoleg.foreign.memory

@ForeignMemoryApi
public actual typealias MemoryAddress = Long

@ForeignMemoryApi
public actual fun nullMemoryAddress(): MemoryAddress = 0

@ForeignMemoryApi
public actual typealias MemoryAddressSize = Long

@ForeignMemoryApi
public actual inline fun memoryAddressSize(value: Int): MemoryAddressSize = value.toLong()

@ForeignMemoryApi
public actual inline fun memoryAddressSize(value: Long): MemoryAddressSize = value

@ForeignMemoryApi
public actual inline fun memoryAddressSizeZero(): MemoryAddressSize = 0
