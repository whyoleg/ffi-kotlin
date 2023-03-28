package dev.whyoleg.foreign.memory

//in bytes
public expect class MemoryAddressSize {

    public operator fun plus(other: MemoryAddressSize): MemoryAddressSize

    public companion object {
        public val SIZE_BYTES: Int
    }
}

public expect inline fun MemoryAddressSize(value: Int): MemoryAddressSize
public expect inline fun MemoryAddressSize(value: Long): MemoryAddressSize

public inline val MemoryAddressSize.Companion.ZERO: MemoryAddressSize get() = ZeroMemoryAddressSize

@PublishedApi
internal expect val ZeroMemoryAddressSize: MemoryAddressSize
