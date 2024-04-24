@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection"
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import dev.whyoleg.foreign.platform.*

class OSSL_PARAM private constructor(segment: MemorySegment) : CStruct<OSSL_PARAM>(segment) {
    override val type: CType.Struct<OSSL_PARAM> get() = Type
    var key: CString? by Type.key
    var data_type: UInt by Type.data_type
    var data: CPointer<Unit>? by Type.data
    var data_size: PlatformUInt by Type.data_size
    var return_size: PlatformUInt by Type.return_size

    override fun toString(): String {
        return "OSSL_PARAM(key=${key}, data_type=$data_type, data_size=$data_size, return_size=$return_size)"
    }

    companion object Type : CType.Struct<OSSL_PARAM>() {
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

expect fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformUInt,
    scope: ForeignCScope = libCrypto3ImplicitScope
): OSSL_PARAM

expect fun OSSL_PARAM_construct_end(
    scope: ForeignCScope = libCrypto3ImplicitScope
): OSSL_PARAM
