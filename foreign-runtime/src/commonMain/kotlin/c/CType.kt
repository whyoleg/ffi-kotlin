package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*

//KT - Kotlin representation, could be: Primitive(Int, Long, etc), Opaque, Struct, Union, Unit(?) or CPointer
@OptIn(ForeignMemoryApi::class)
@SubclassOptInRequired(ForeignMemoryApi::class)
public abstract class CType<KT> {
    public abstract val layout: MemoryLayout
    public abstract fun accessor(offset: MemoryAddressSize): MemoryAccessor<KT>

    public object Byte : CType<kotlin.Byte>() {
        override val layout: MemoryLayout get() = MemoryLayout.Byte
        override fun accessor(offset: MemoryAddressSize): MemoryAccessor<kotlin.Byte> = MemoryAccessor.Byte(offset)
    }

    public object UByte : CType<kotlin.UByte>()
    public object Int : CType<kotlin.Int>()
    public object UInt : CType<kotlin.UInt>()
    public object Long : CType<kotlin.Long>()
    public object ULong : CType<kotlin.ULong>()
    public object PlatformInt : CType<dev.whyoleg.foreign.PlatformInt>() {
        override val layout: MemoryLayout get() = MemoryLayout.PlatformInt
        override fun accessor(offset: MemoryAddressSize): MemoryAccessor<dev.whyoleg.foreign.PlatformInt> =
            MemoryAccessor.PlatformInt(offset)
    }

    public object PlatformUInt : CType<dev.whyoleg.foreign.PlatformUInt>()
    public object Unit : CType<kotlin.Unit>()

    public class Pointer<KT>(
        private val pointedType: CType<KT>,
    ) : CType<CPointer<KT>?>() {
        override val layout: MemoryLayout get() = MemoryLayout.Address
        override fun accessor(offset: MemoryAddressSize): MemoryAccessor<CPointer<KT>?> =
            CPointer.Accessor(pointedType.layout, pointedType.accessor(0), offset)
    }

    public abstract class Struct<KT : CStruct> : CType<KT>() {
        private val _layout = MemoryLayoutImpl()
        override val layout: MemoryLayout get() = MemoryLayoutImpl()

        protected fun <KT> element(type: CType<KT>): MemoryAccessor<KT> = _layout.register(type)

        private class MemoryLayoutImpl : MemoryLayout.Struct {
            private var _size: MemoryAddressSize = 0
            override val alignment: MemoryAddressSize
                get() = TODO("Not yet implemented")
            override val size: MemoryAddressSize
                get() = _size

            fun <KT> register(type: CType<KT>): MemoryAccessor<KT> {
                return type.accessor(_size).also {
                    _size += type.layout.size //TODO
                }
            }
        }
    }
}
