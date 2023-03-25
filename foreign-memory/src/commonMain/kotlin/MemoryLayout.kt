package dev.whyoleg.foreign.memory

//public interface MemoryAccessor

//KT - Kotlin representation, could be: Primitive(Int, Long, etc), Opaque, Struct, Union, Unit(?) or CPointer
public interface MemoryLayout<KT : Any> {
    public val alignment: MemoryAddressSize
    public val size: MemoryAddressSize

    public fun accessor(offset: MemoryAddressSize = 0)

    @Suppress("FunctionName")
    public companion object {
        public fun Byte(): MemoryLayout<Byte> = ByteLayout

        @Suppress("UNCHECKED_CAST")
        public fun <KT : MemoryHolder> Address(): MemoryLayout<KT> = AddressLayout as MemoryLayout<KT>
    }

//    public object UByte : Primitive<kotlin.Byte>(kotlin.UByte.SIZE_BYTES)
//    public object Int : Primitive<kotlin.Int>(kotlin.Int.SIZE_BYTES)
//    public object UInt : Primitive<kotlin.Int>(kotlin.UInt.SIZE_BYTES)
//    public object Long : Primitive<kotlin.Long>(kotlin.Long.SIZE_BYTES)
//    public object ULong : Primitive<kotlin.Long>(kotlin.ULong.SIZE_BYTES)
//    public object PlatformInt : Primitive<dev.whyoleg.foreign.platform.PlatformInt>(dev.whyoleg.foreign.platform.PlatformInt.SIZE_BYTES)
}

private object ByteLayout : MemoryLayout<Byte> {
    override val alignment: MemoryAddressSize get() = Byte.SIZE_BYTES
    override val size: MemoryAddressSize get() = Byte.SIZE_BYTES
}

private object AddressLayout : MemoryLayout<MemoryHolder> {
    override val alignment: MemoryAddressSize get() = MemoryAddressSize.SIZE_BYTES
    override val size: MemoryAddressSize get() = MemoryAddressSize.SIZE_BYTES
}
