@file:Suppress("ClassName", "PropertyName")

package dev.whyoleg.foreign.lib

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import dev.whyoleg.foreign.platform.*

@OptIn(ForeignMemoryApi::class)
public class EVP_PKEY private constructor() : COpaque() {
    public companion object Type : CType.Opaque<EVP_PKEY>() {
        @ForeignMemoryApi
        override val accessor: OpaqueMemoryAccessor<EVP_PKEY> = OpaqueMemoryAccessor(EVP_PKEY())
    }
}

// TODO: we can use compiler plugin to generate `Type` similar to how kx.serialization works
@OptIn(ForeignMemoryApi::class)
public class OSSL_PARAM private constructor(segment: MemorySegment) : CStruct<OSSL_PARAM>(segment) {
    override val type: CType.Struct<OSSL_PARAM> get() = Type
    public var key: CString? by Type.key
    public var data_type: UInt by Type.data_type
    public var data: CPointer<Unit>? by Type.data
    public var data_size: PlatformUInt by Type.data_size
    public var return_size: PlatformUInt by Type.return_size

    public companion object Type : CType.Struct<OSSL_PARAM>() {
        private val key = element(Byte.pointer)
        private val data_type = element(UInt)
        private val data = element(Void.pointer)
        private val data_size = element(PlatformUInt)
        private val return_size = element(PlatformUInt)

        override val accessor: ValueMemoryAccessor<OSSL_PARAM> get() = Accessor

        private open class Accessor private constructor(offset: MemoryAddressSize) : ValueMemoryAccessor<OSSL_PARAM>(offset) {
            override val layout: MemoryLayout get() = Type.layout
            override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<OSSL_PARAM> = Accessor(offset)
            override fun wrap(segment: MemorySegment): OSSL_PARAM = OSSL_PARAM(segment)

            companion object : Accessor(memoryAddressSizeZero())
        }
    }
}
