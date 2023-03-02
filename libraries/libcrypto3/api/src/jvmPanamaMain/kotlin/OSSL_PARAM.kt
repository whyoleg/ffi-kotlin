@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
import java.lang.foreign.*
import java.lang.invoke.*

//TODO: likely `data` should be layout.asUnbounded()
internal val OSSL_PARAM_layout = MemoryLayout.structLayout(
    ValueLayout.ADDRESS.withName("key").asUnbounded(), //8
    ValueLayout.JAVA_INT.withName("data_type"), //4
    MemoryLayout.paddingLayout(32), //TODO: how this works ? :) //4
    ValueLayout.ADDRESS.withName("data"), //8
    ValueLayout.JAVA_LONG.withName("data_size"), //8
    ValueLayout.JAVA_LONG.withName("return_size"), //8
).withName("ossl_param_st")  //TODO: or OSSL_PARAM?

private val key_VH = OSSL_PARAM_layout.varHandle(MemoryLayout.PathElement.groupElement("key"))
private val data_type_VH = OSSL_PARAM_layout.varHandle(MemoryLayout.PathElement.groupElement("data_type"))
private val data_VH = OSSL_PARAM_layout.varHandle(MemoryLayout.PathElement.groupElement("data"))
private val data_size_VH = OSSL_PARAM_layout.varHandle(MemoryLayout.PathElement.groupElement("data_size"))
private val return_size_VH = OSSL_PARAM_layout.varHandle(MemoryLayout.PathElement.groupElement("return_size"))

actual object OSSL_PARAM_Type : CVariableType<OSSL_PARAM>() {
    override val layout: NativeLayout get() = NativeLayout.of(OSSL_PARAM_layout)
    override fun wrap(memory: NativeMemory): OSSL_PARAM = OSSL_PARAM(memory)
}

actual class OSSL_PARAM(memory: NativeMemory) : CStructVariable(memory) {
    override val type: CPointedType<*> get() = OSSL_PARAM_Type
}

actual var OSSL_PARAM.key: CString?
    get() = nativeCString(key_VH.get(nativeAddress) as MemorySegment)
    set(value) = key_VH.set(nativeAddress, value.nativeAddress)

actual var OSSL_PARAM.data_type: UInt
    get() = (data_type_VH.get(nativeAddress) as Int).toUInt()
    set(value) = data_type_VH.set(nativeAddress, value.toInt())

actual var OSSL_PARAM.data: CPointer<out CPointed>?
    get() = nativeCPointer(COpaqueTypeEmpty, data_VH.get(nativeAddress) as MemorySegment)
    set(value) = data_VH.set(nativeAddress, value.nativeAddress)

actual var OSSL_PARAM.data_size: PlatformDependentUInt
    get() = (data_size_VH.get(nativeAddress) as Long).toULong()
    set(value) = data_size_VH.set(nativeAddress, value.toLong())

actual var OSSL_PARAM.return_size: PlatformDependentUInt
    get() = (return_size_VH.get(nativeAddress) as Long).toULong()
    set(value) = return_size_VH.set(nativeAddress, value.toLong())

private val OSSL_PARAM_construct_utf8_string_MH: MethodHandle = FFI.methodHandle(
    name = "OSSL_PARAM_construct_utf8_string",
    result = OSSL_PARAM_layout,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformDependentUInt,
): CValue<OSSL_PARAM> = nativeCValue(OSSL_PARAM_Type) { allocator ->
    OSSL_PARAM_construct_utf8_string_MH.invokeExact(
        allocator,
        key.nativeAddress,
        buf.nativeAddress,
        bsize.toLong()
    ) as MemorySegment
}

private val OSSL_PARAM_construct_end_MH: MethodHandle = FFI.methodHandle(
    name = "OSSL_PARAM_construct_end",
    result = OSSL_PARAM_layout
)

actual fun OSSL_PARAM_construct_end(): CValue<OSSL_PARAM> = nativeCValue(OSSL_PARAM_Type) { allocator ->
    OSSL_PARAM_construct_end_MH.invokeExact(allocator) as MemorySegment
}
