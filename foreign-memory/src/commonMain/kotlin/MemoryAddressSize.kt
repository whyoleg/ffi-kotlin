package dev.whyoleg.foreign.memory

//in bytes
public expect class MemoryAddressSize : Comparable<MemoryAddressSize> {

    public operator fun plus(other: MemoryAddressSize): MemoryAddressSize

    public companion object {
        public val SIZE_BYTES: Int
    }
}

public expect inline fun memoryAddressSize(value: Int): MemoryAddressSize
public expect inline fun memoryAddressSize(value: Long): MemoryAddressSize
public expect inline fun memoryAddressSizeZero(): MemoryAddressSize
