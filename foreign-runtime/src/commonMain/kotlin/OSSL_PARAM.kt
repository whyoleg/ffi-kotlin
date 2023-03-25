package dev.whyoleg.foreign

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.memory.*

@OptIn(ForeignMemoryApi::class)
@Suppress("ClassName", "PropertyName")
public class OSSL_PARAM private constructor(segment: MemorySegment) : CStruct(segment) {
    public var key: CString? by Type.key
    public var data_type: UInt by Type.data_type
    public var data: CPointer<Unit>? by Type.data
    public var data_size: PlatformUInt by Type.data_size
    public var return_size: PlatformUInt by Type.return_size

    @OptIn(ForeignMemoryApi::class)
    public companion object Type : CType.Struct<OSSL_PARAM>() {
        private val key = element(Pointer(Byte))
        private val data_type = element(UInt)
        private val data = element(Pointer(Unit))
        private val data_size = element(PlatformUInt)
        private val return_size = element(PlatformUInt)

        override fun accessor(offset: MemoryAddressSize): MemoryAccessor<OSSL_PARAM> = Accessor(offset)

        private class Accessor(override val offset: MemoryAddressSize) : MemoryAccessor<OSSL_PARAM>() {
            override fun get(segment: MemorySegment): OSSL_PARAM = OSSL_PARAM(segment.loadSegment(offset, layout))
            override fun set(segment: MemorySegment, value: OSSL_PARAM): kotlin.Unit = segment.storeSegment(offset, layout, value.segment)
        }
    }
}
