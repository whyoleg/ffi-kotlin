package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import dev.whyoleg.foreign.platform.PlatformInt as PInt
import dev.whyoleg.foreign.platform.PlatformUInt as PUInt

public sealed class CType<KT : Any> {
    @ForeignMemoryApi
    public abstract val layout: MemoryLayout

    @ForeignMemoryApi
    public abstract val accessor: MemoryAccessor<KT>

    public val pointer: CType<CPointer<KT>> by lazy { Pointer(this) }

    public object Void : CType<Unit>() {
        @ForeignMemoryApi
        override val layout: MemoryLayout get() = MemoryLayout.Void

        @ForeignMemoryApi
        override val accessor: MemoryAccessor<Unit> get() = MemoryAccessor.Void
    }

    public object Byte : CType<kotlin.Byte>() {
        @ForeignMemoryApi
        override val layout: MemoryLayout get() = MemoryLayout.Byte

        @ForeignMemoryApi
        override val accessor: MemoryAccessor<kotlin.Byte> get() = MemoryAccessor.Byte
    }

    public object Int : CType<kotlin.Int>() {
        @ForeignMemoryApi
        override val layout: MemoryLayout get() = MemoryLayout.Int

        @ForeignMemoryApi
        override val accessor: MemoryAccessor<kotlin.Int> get() = MemoryAccessor.Int
    }

    public object UInt : CType<kotlin.UInt>() {
        @ForeignMemoryApi
        override val layout: MemoryLayout get() = MemoryLayout.Int

        @ForeignMemoryApi
        override val accessor: MemoryAccessor<kotlin.UInt> get() = MemoryAccessor.UInt
    }

    public object PlatformInt : CType<PInt>() {
        @ForeignMemoryApi
        override val layout: MemoryLayout get() = MemoryLayout.PlatformInt

        @ForeignMemoryApi
        override val accessor: MemoryAccessor<PInt> get() = MemoryAccessor.PlatformInt
    }

    public object PlatformUInt : CType<PUInt>() {
        @ForeignMemoryApi
        override val layout: MemoryLayout get() = MemoryLayout.PlatformInt

        @ForeignMemoryApi
        override val accessor: MemoryAccessor<PUInt> get() = MemoryAccessor.PlatformUInt
    }

    public abstract class Struct<KT : CStruct<KT>> : CType<KT>() {
        @ForeignMemoryApi
        private val _layout = StructLayout()

        @ForeignMemoryApi
        final override val layout: MemoryLayout get() = _layout

        @ForeignMemoryApi
        protected fun <KT : Any> element(type: CType<KT>): MemoryAccessor<KT> = type.accessor.at(_layout.offset(type.layout))

        @ForeignMemoryApi
        private class StructLayout() : MemoryLayout {
            private var _size: MemoryAddressSize = memoryAddressSizeZero()
            override val alignment: MemoryAddressSize get() = memoryAddressSize(8) // TODO!!!
            override val size: MemoryAddressSize get() = _size

            // TODO!!!
            fun offset(layout: MemoryLayout): MemoryAddressSize {
                val offset = _size
                _size += layout.size
                return offset
            }
        }
    }

    private class Pointer<KT : Any>(pointedType: CType<KT>) : CType<CPointer<KT>>() {
        @ForeignMemoryApi
        override val layout: MemoryLayout get() = MemoryLayout.Address

        @ForeignMemoryApi
        override val accessor: MemoryAccessor<CPointer<KT>> = Accessor(pointedType.accessor)

        @ForeignMemoryApi
        private class Accessor<KT : Any>(
            override val pointedAccessor: MemoryAccessor<KT>,
            offset: MemoryAddressSize = memoryAddressSizeZero(),
        ) : AddressMemoryAccessor<CPointer<KT>, KT>(offset) {
            override fun at(offset: MemoryAddressSize): MemoryAccessor<CPointer<KT>> = Accessor(pointedAccessor, offset)
            override fun wrap(segment: MemorySegment): CPointer<KT> = CPointer(pointedAccessor, segment)
        }
    }
}
