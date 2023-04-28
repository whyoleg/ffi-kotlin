package dev.whyoleg.foreign.memory

//in bytes
@ForeignMemoryApi
public expect class MemoryAddressSize : Comparable<MemoryAddressSize> {

    public operator fun plus(other: MemoryAddressSize): MemoryAddressSize
    public operator fun times(other: Int): MemoryAddressSize
    public operator fun rem(other: Int): MemoryAddressSize

    public companion object {
        public val SIZE_BYTES: Int
    }
}

@ForeignMemoryApi
public expect inline fun memoryAddressSize(value: Int): MemoryAddressSize

@ForeignMemoryApi
public expect inline fun memoryAddressSize(value: Long): MemoryAddressSize

@ForeignMemoryApi
public expect inline fun memoryAddressSizeZero(): MemoryAddressSize
