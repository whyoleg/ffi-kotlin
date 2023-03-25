package dev.whyoleg.foreign.memory

import kotlin.reflect.*

//TODO: rename to MemoryHandle?
//KT - Kotlin representation, could be: Primitive(Int, Long, etc), Opaque, Struct, Union, Unit(?) or CPointer
public sealed class MemoryLayoutX<KT : Any> {
    public abstract val alignment: MemoryAddressSize
    public abstract val size: MemoryAddressSize
    public abstract val offset: MemoryAddressSize

    public class Byte(override val offset: MemoryAddressSize = 0) : MemoryLayout<kotlin.Byte>() {
        override val size: MemoryAddressSize get() = kotlin.Byte.SIZE_BYTES
        override val alignment: MemoryAddressSize get() = kotlin.Byte.SIZE_BYTES

        @ForeignMemoryApi
        public inline operator fun getValue(thisRef: MemoryHolder, property: KProperty<*>): kotlin.Byte =
            thisRef.segment.loadByte(offset)

        @ForeignMemoryApi
        public inline operator fun setValue(thisRef: MemoryHolder, property: KProperty<*>, value: kotlin.Byte): Unit =
            thisRef.segment.storeByte(offset, value)
    }

//    public object UByte : Primitive<kotlin.Byte>(kotlin.UByte.SIZE_BYTES)
//    public object Int : Primitive<kotlin.Int>(kotlin.Int.SIZE_BYTES)
//    public object UInt : Primitive<kotlin.Int>(kotlin.UInt.SIZE_BYTES)
//    public object Long : Primitive<kotlin.Long>(kotlin.Long.SIZE_BYTES)
//    public object ULong : Primitive<kotlin.Long>(kotlin.ULong.SIZE_BYTES)
//    public object PlatformInt : Primitive<dev.whyoleg.foreign.platform.PlatformInt>(dev.whyoleg.foreign.platform.PlatformInt.SIZE_BYTES)

    public abstract class Address<KT : MemoryHolder>(final override val offset: MemoryAddressSize) : MemoryLayout<KT>() {
        final override val alignment: MemoryAddressSize get() = MemoryAddressSize.SIZE_BYTES
        final override val size: MemoryAddressSize get() = MemoryAddressSize.SIZE_BYTES

        @ForeignMemoryApi
        protected abstract fun wrap(segment: MemorySegment): KT

        @PublishedApi
        @ForeignMemoryApi
        internal fun wrapInternal(segment: MemorySegment): KT = wrap(segment)

        @ForeignMemoryApi
        public inline operator fun getValue(thisRef: MemoryHolder, property: KProperty<*>): KT? =
            thisRef.segment.loadAddress(offset)?.let(::wrapInternal)

        @ForeignMemoryApi
        public inline operator fun setValue(thisRef: MemoryHolder, property: KProperty<*>, value: KT?): Unit =
            thisRef.segment.storeAddress(offset, value?.segment)
    }

    public abstract class Segment<KT : MemoryHolder>(final override val offset: MemoryAddressSize) : MemoryLayout<KT>() {

        @ForeignMemoryApi
        protected abstract fun wrap(segment: MemorySegment): KT

        @PublishedApi
        @ForeignMemoryApi
        internal fun wrapInternal(segment: MemorySegment): KT = wrap(segment)

        @ForeignMemoryApi
        public inline operator fun getValue(thisRef: MemoryHolder, property: KProperty<*>): KT =
            wrapInternal(thisRef.segment.loadSegment(offset, this))

        @ForeignMemoryApi
        public inline operator fun setValue(thisRef: MemoryHolder, property: KProperty<*>, value: KT): Unit =
            thisRef.segment.storeSegment(offset, this, value.segment)
    }
}
