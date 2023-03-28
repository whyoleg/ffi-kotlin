package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.platform.PlatformInt as PInt

//KT - Kotlin representation, could be: Primitive(Int, Long, etc), Opaque, Struct, Union, Unit(?) or CPointer
@ForeignMemoryApi
public interface MemoryLayout {
    public val alignment: MemoryAddressSize
    public val size: MemoryAddressSize

    // TODO: naming
    public object Void : MemoryLayout {
        override val alignment: MemoryAddressSize get() = MemoryAddressSize(0)
        override val size: MemoryAddressSize get() = MemoryAddressSize(0)
    }

    public object Byte : MemoryLayout {
        override val alignment: MemoryAddressSize get() = MemoryAddressSize(kotlin.Byte.SIZE_BYTES)
        override val size: MemoryAddressSize get() = MemoryAddressSize(kotlin.Byte.SIZE_BYTES)
    }

    public object Int : MemoryLayout {
        override val alignment: MemoryAddressSize get() = MemoryAddressSize(kotlin.Int.SIZE_BYTES)
        override val size: MemoryAddressSize get() = MemoryAddressSize(kotlin.Int.SIZE_BYTES)
    }

    public object Long : MemoryLayout {
        override val alignment: MemoryAddressSize get() = MemoryAddressSize(kotlin.Long.SIZE_BYTES)
        override val size: MemoryAddressSize get() = MemoryAddressSize(kotlin.Long.SIZE_BYTES)
    }

    public object PlatformInt : MemoryLayout {
        override val alignment: MemoryAddressSize get() = MemoryAddressSize(PInt.SIZE_BYTES)
        override val size: MemoryAddressSize get() = MemoryAddressSize(PInt.SIZE_BYTES)
    }

    public object Address : MemoryLayout {
        override val alignment: MemoryAddressSize get() = MemoryAddressSize(MemoryAddressSize.SIZE_BYTES)
        override val size: MemoryAddressSize get() = MemoryAddressSize(MemoryAddressSize.SIZE_BYTES)
    }

//    public object UByte : Primitive<kotlin.Byte>(kotlin.UByte.SIZE_BYTES)
//    public object Int : Primitive<kotlin.Int>(kotlin.Int.SIZE_BYTES)
//    public object UInt : Primitive<kotlin.Int>(kotlin.UInt.SIZE_BYTES)
//    public object Long : Primitive<kotlin.Long>(kotlin.Long.SIZE_BYTES)
//    public object ULong : Primitive<kotlin.Long>(kotlin.ULong.SIZE_BYTES)
//    public object PlatformInt : Primitive<dev.whyoleg.foreign.platform.PlatformInt>(dev.whyoleg.foreign.platform.PlatformInt.SIZE_BYTES)
}
