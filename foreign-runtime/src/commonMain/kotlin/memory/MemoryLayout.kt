package dev.whyoleg.foreign.memory

@ForeignMemoryApi
public sealed interface MemoryLayout {
    public val alignment: MemoryAddressSize
    public val size: MemoryAddressSize

    public sealed class Primitive(override val size: MemoryAddressSize) : MemoryLayout {
        override val alignment: MemoryAddressSize get() = size
    }

    public object Byte : Primitive(kotlin.Byte.SIZE_BYTES)
    public object Int : Primitive(kotlin.Int.SIZE_BYTES)
    public object Long : Primitive(kotlin.Long.SIZE_BYTES)
    public object PlatformInt : Primitive(dev.whyoleg.foreign.PlatformInt.SIZE_BYTES)
    public object Address : Primitive(MemoryAddressSize.SIZE_BYTES)

    //TODO?
    public interface Struct : MemoryLayout
}
