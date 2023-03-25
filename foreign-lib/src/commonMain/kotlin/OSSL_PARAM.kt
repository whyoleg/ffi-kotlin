package dev.whyoleg.foreign.lib

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*

@OptIn(ForeignMemoryApi::class)
@Suppress("ClassName", "PropertyName")
public class OSSL_PARAM private constructor(segment: MemorySegment) : CStruct<OSSL_PARAM>(segment) {
    override val type: CType.Struct<OSSL_PARAM> get() = Type
    public var key: CString? by Type.key

    public var data_type: UInt by Type.data_type
    public var data: CPointer<Unit>? by Type.data
//    public var data_size: PlatformUInt by Layout.data_size
//    public var return_size: PlatformUInt by Layout.return_size

    public companion object Type : CType.Struct<OSSL_PARAM>() {
        private val key = element(Pointer(Byte))

        private val data_type = element(UInt)
        private val data = element(Pointer(Void))
        private val nested = element(OSSL_PARAM)

        override val accessor: MemoryAccessor<OSSL_PARAM> get() = Accessor

        private open class Accessor private constructor(offset: MemoryAddressSize) : MemoryAccessor.Segment<OSSL_PARAM>(offset) {
            override val layout: MemoryLayout get() = Type.layout
            override fun at(offset: MemoryAddressSize): MemoryAccessor<OSSL_PARAM> = Accessor(offset)
            override fun wrap(segment: MemorySegment): OSSL_PARAM = OSSL_PARAM(segment)

            companion object : Accessor(0)
        }
    }
}
