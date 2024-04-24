package dev.whyoleg.foreign.c

public sealed interface CType<KT> {
    public interface Opaque<KT> : CType<KT>
    public interface Record<KT> : CType<KT>

    public object Builtin {
        public val Byte: CType<Byte> get() = TODO()
    }
}

// TODO: needs compiler plugin
//public inline fun <reified KT> cTypeOf(): CType<KT> = TODO()

//@OptIn(ForeignMemoryApi::class)
//public sealed class CType<KT : Any> {
//    @ForeignMemoryApi
//    public abstract val accessor: MemoryAccessor<KT>
//
//    @ForeignMemoryApi
//    public open val layout: MemoryBlockLayout get() = accessor.layout
//
//    public val pointer: CType<CPointer<KT>> by lazy { Pointer(this) }
//
//    public object Void : CType<Unit>() {
//        override val accessor: MemoryAccessor<Unit> get() = MemoryAccessor.Void
//    }
//
//    public object Byte : CType<kotlin.Byte>() {
//        override val accessor: MemoryAccessor<kotlin.Byte> get() = MemoryAccessor.Byte
//    }
//
//    public object UByte : CType<kotlin.UByte>() {
//        override val accessor: MemoryAccessor<kotlin.UByte> get() = MemoryAccessor.UByte
//    }
//
//    public object Int : CType<kotlin.Int>() {
//        override val accessor: MemoryAccessor<kotlin.Int> get() = MemoryAccessor.Int
//    }
//
//    public object UInt : CType<kotlin.UInt>() {
//        override val accessor: MemoryAccessor<kotlin.UInt> get() = MemoryAccessor.UInt
//    }
//
//    public object PlatformInt : CType<PInt>() {
//        override val accessor: MemoryAccessor<PInt> get() = MemoryAccessor.PlatformInt
//    }
//
//    public object PlatformUInt : CType<PUInt>() {
//        override val accessor: MemoryAccessor<PUInt> get() = MemoryAccessor.PlatformUInt
//    }
//
//    public abstract class Opaque<KT : COpaque>(instance: KT) : CType<KT>() {
//        final override val accessor: OpaqueMemoryAccessor<KT> = OpaqueMemoryAccessor(instance)
//    }
//
//    public sealed class Record<KT : CRecord<KT>>(isUnion: Boolean) : CType<KT>() {
//        private val _layout = MemoryBlockLayout.Record(isUnion)
//        final override val layout: MemoryBlockLayout get() = _layout
//        abstract override val accessor: ValueMemoryAccessor<KT>
//
//        protected fun <KT : Any> element(type: CType<KT>): MemoryAccessor<KT> {
//            return type.accessor.at(_layout.getOffsetAndAddField(type.layout))
//        }
//    }
//
//    public abstract class Struct<KT : CStruct<KT>> : Record<KT>(isUnion = false)
//    public abstract class Union<KT : CUnion<KT>> : Record<KT>(isUnion = true)
//
//    private class Pointer<KT : Any>(pointedType: CType<KT>) : CType<CPointer<KT>>() {
//        override val accessor: MemoryAccessor<CPointer<KT>> = Accessor(pointedType.accessor)
//
//        private class Accessor<KT : Any>(
//            override val pointedAccessor: MemoryAccessor<KT>,
//            offset: MemoryAddressSize = memoryAddressSizeZero(),
//        ) : ReferenceMemoryAccessor<CPointer<KT>, KT>(offset) {
//            override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<CPointer<KT>> = Accessor(pointedAccessor, offset)
//            override fun wrapPointed(pointedSegment: MemoryBlock): CPointer<KT> = CPointer(pointedAccessor, pointedSegment)
//        }
//    }
//}
