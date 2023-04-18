package dev.whyoleg.foreign.memory

import kotlin.native.internal.*

@ForeignMemoryApi
public actual typealias MemoryAddress = NativePtr

@ForeignMemoryApi
public actual fun nullMemoryAddress(): MemoryAddress = NativePtr.NULL

@ForeignMemoryApi
public actual typealias MemoryAddressSize = Long

@ForeignMemoryApi
public actual inline fun memoryAddressSize(value: Int): MemoryAddressSize = value.toLong()

@ForeignMemoryApi
public actual inline fun memoryAddressSize(value: Long): MemoryAddressSize = value

@ForeignMemoryApi
public actual inline fun memoryAddressSizeZero(): MemoryAddressSize = 0
