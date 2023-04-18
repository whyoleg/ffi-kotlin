package dev.whyoleg.foreign.memory

import java.lang.foreign.MemorySegment as JMemorySegment

@Suppress("ACTUAL_WITHOUT_EXPECT")
@ForeignMemoryApi
public actual typealias MemoryAddress = JMemorySegment

@ForeignMemoryApi
public actual fun nullMemoryAddress(): MemoryAddress = JMemorySegment.NULL

@ForeignMemoryApi
public actual typealias MemoryAddressSize = Long

@ForeignMemoryApi
public actual inline fun memoryAddressSize(value: Int): MemoryAddressSize = value.toLong()

@ForeignMemoryApi
public actual inline fun memoryAddressSize(value: Long): MemoryAddressSize = value

@ForeignMemoryApi
public actual inline fun memoryAddressSizeZero(): MemoryAddressSize = 0
