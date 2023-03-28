package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.platform.PlatformInt as PInt

@ForeignMemoryApi
public interface MemoryLayout {
    public val alignment: MemoryAddressSize
    public val size: MemoryAddressSize

    // TODO: naming
    public object Void : MemoryLayout {
        override val alignment: MemoryAddressSize get() = memoryAddressSize(0)
        override val size: MemoryAddressSize get() = memoryAddressSize(0)
    }

    public object Byte : MemoryLayout {
        override val alignment: MemoryAddressSize get() = memoryAddressSize(kotlin.Byte.SIZE_BYTES)
        override val size: MemoryAddressSize get() = memoryAddressSize(kotlin.Byte.SIZE_BYTES)
    }

    public object Int : MemoryLayout {
        override val alignment: MemoryAddressSize get() = memoryAddressSize(kotlin.Int.SIZE_BYTES)
        override val size: MemoryAddressSize get() = memoryAddressSize(kotlin.Int.SIZE_BYTES)
    }

    public object Long : MemoryLayout {
        override val alignment: MemoryAddressSize get() = memoryAddressSize(kotlin.Long.SIZE_BYTES)
        override val size: MemoryAddressSize get() = memoryAddressSize(kotlin.Long.SIZE_BYTES)
    }

    public object PlatformInt : MemoryLayout {
        override val alignment: MemoryAddressSize get() = memoryAddressSize(PInt.SIZE_BYTES)
        override val size: MemoryAddressSize get() = memoryAddressSize(PInt.SIZE_BYTES)
    }

    public object Address : MemoryLayout {
        override val alignment: MemoryAddressSize get() = memoryAddressSize(MemoryAddressSize.SIZE_BYTES)
        override val size: MemoryAddressSize get() = memoryAddressSize(MemoryAddressSize.SIZE_BYTES)
    }
}
