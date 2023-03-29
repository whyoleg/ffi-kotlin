package dev.whyoleg.foreign.memory

@Suppress("NO_ACTUAL_FOR_EXPECT") //because MemorySegment on JVM is an interface
@ForeignMemoryApi
public expect class MemoryAddress

//in bytes
@ForeignMemoryApi
public expect class MemoryAddressSize : Comparable<MemoryAddressSize> {

    public operator fun plus(other: MemoryAddressSize): MemoryAddressSize

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


//TODO: introduce some kind of `InteropScope`, only in which we can access unsafe things
//TODO: somehow hide those values...
//TODO: create additional package like foreign.invoke/foreign.function/foreign.interop, etc
@ForeignMemoryApi
public val MemoryHolder?.address: MemoryAddress get() = (this?.segment ?: MemorySegment.Empty).address
