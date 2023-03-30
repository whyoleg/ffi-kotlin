package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import dev.whyoleg.foreign.platform.PlatformInt as PInt
import dev.whyoleg.foreign.platform.PlatformUInt as PUInt

@OptIn(ForeignMemoryApi::class)
public sealed class CType<KT : Any> {
    @ForeignMemoryApi
    public abstract val layout: MemoryLayout

    @ForeignMemoryApi
    public abstract val accessor: MemoryAccessor<KT>

    public val pointer: CType<CPointer<KT>> by lazy { Pointer(this) }

    public object Void : CType<Unit>() {
        override val layout: MemoryLayout get() = MemoryLayout.Void
        override val accessor: MemoryAccessor<Unit> get() = MemoryAccessor.Void
    }

    public object Byte : CType<kotlin.Byte>() {
        override val layout: MemoryLayout get() = MemoryLayout.Byte
        override val accessor: MemoryAccessor<kotlin.Byte> get() = MemoryAccessor.Byte
    }

    public object Int : CType<kotlin.Int>() {
        override val layout: MemoryLayout get() = MemoryLayout.Int
        override val accessor: MemoryAccessor<kotlin.Int> get() = MemoryAccessor.Int
    }

    public object UInt : CType<kotlin.UInt>() {
        override val layout: MemoryLayout get() = MemoryLayout.Int
        override val accessor: MemoryAccessor<kotlin.UInt> get() = MemoryAccessor.UInt
    }

    public object PlatformInt : CType<PInt>() {
        override val layout: MemoryLayout get() = MemoryLayout.PlatformInt
        override val accessor: MemoryAccessor<PInt> get() = MemoryAccessor.PlatformInt
    }

    public object PlatformUInt : CType<PUInt>() {
        override val layout: MemoryLayout get() = MemoryLayout.PlatformInt
        override val accessor: MemoryAccessor<PUInt> get() = MemoryAccessor.PlatformUInt
    }

    public abstract class Opaque<KT : COpaque>(instance: KT) : CType<KT>() {
        final override val layout: MemoryLayout get() = MemoryLayout.Void
        final override val accessor: OpaqueMemoryAccessor<KT> = OpaqueMemoryAccessor(instance)
    }

    public sealed class Group<KT : CGrouped<KT>> : CType<KT>() {
        abstract override val accessor: ValueMemoryAccessor<KT>
    }

    public abstract class Struct<KT : CStruct<KT>> : Group<KT>() {
        private val _layout = StructLayout()
        final override val layout: MemoryLayout get() = _layout

        protected fun <KT : Any> element(type: CType<KT>): MemoryAccessor<KT> {
            val offset = _layout.register(type.layout)
            return type.accessor.at(offset)
        }

        private class StructLayout() : MemoryLayout {
            private var _size: MemoryAddressSize = memoryAddressSizeZero()
            private var _alignment: MemoryAddressSize = memoryAddressSizeZero()

            override val size: MemoryAddressSize get() = _size
            override val alignment: MemoryAddressSize get() = _alignment

            //TODO: recheck
            fun register(layout: MemoryLayout): MemoryAddressSize {
                val offset = _size
                //TODO: alignment should affect offset?
                _size += layout.size
                if (_alignment < layout.alignment) {
                    _alignment = layout.alignment
                }
                return offset
            }
        }
    }

    public abstract class Union<KT : CUnion<KT>> : Group<KT>() {
        private val _layout = UnionLayout()
        final override val layout: MemoryLayout get() = _layout

        protected fun <KT : Any> element(type: CType<KT>): MemoryAccessor<KT> {
            _layout.register(type.layout)
            return type.accessor
        }

        private class UnionLayout() : MemoryLayout {
            private var _size: MemoryAddressSize = memoryAddressSizeZero()
            private var _alignment: MemoryAddressSize = memoryAddressSizeZero()

            override val size: MemoryAddressSize get() = _size
            override val alignment: MemoryAddressSize get() = _alignment

            //TODO: recheck
            fun register(layout: MemoryLayout) {
                if (_size < layout.size) {
                    _size = layout.size
                }
                if (_alignment < layout.alignment) {
                    _alignment = layout.alignment
                }
            }
        }
    }

    private class Pointer<KT : Any>(pointedType: CType<KT>) : CType<CPointer<KT>>() {
        override val layout: MemoryLayout get() = MemoryLayout.Address
        override val accessor: MemoryAccessor<CPointer<KT>> = Accessor(pointedType.accessor)

        private class Accessor<KT : Any>(
            override val pointedAccessor: MemoryAccessor<KT>,
            offset: MemoryAddressSize = memoryAddressSizeZero(),
        ) : ReferenceMemoryAccessor<CPointer<KT>, KT>(offset) {
            override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<CPointer<KT>> = Accessor(pointedAccessor, offset)
            override fun wrapPointed(pointedSegment: MemorySegment): CPointer<KT> = CPointer(pointedAccessor, pointedSegment)
        }
    }
}
