@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*
import java.lang.foreign.*
import java.lang.invoke.*

//TODO: likely addresses should be layout.asUnbounded()
private val layout = MemoryLayout.structLayout(
    ValueLayout.ADDRESS.withName("key"), //8
    ValueLayout.JAVA_INT.withName("data_type"), //4
    MemoryLayout.paddingLayout(32), //TODO: how this works ? :) //4
    ValueLayout.ADDRESS.withName("data"), //8
    ValueLayout.JAVA_LONG.withName("data_size"), //8
    ValueLayout.JAVA_LONG.withName("return_size"), //8
).withName("ossl_param_st")  //TODO: or OSSL_PARAM?

private val key_VH = layout.varHandle(MemoryLayout.PathElement.groupElement("key"))
private val data_type_VH = layout.varHandle(MemoryLayout.PathElement.groupElement("data_type"))
private val data_VH = layout.varHandle(MemoryLayout.PathElement.groupElement("data"))
private val data_size_VH = layout.varHandle(MemoryLayout.PathElement.groupElement("data_size"))
private val return_size_VH = layout.varHandle(MemoryLayout.PathElement.groupElement("return_size"))

actual object OSSL_PARAM_Type : CVariableType<OSSL_PARAM>(::OSSL_PARAM, layout)

actual class OSSL_PARAM(segment: MemorySegment) : CStructVariable(segment)

actual var OSSL_PARAM.key: CString?
    get() = CString(key_VH.get(segment.also { println(it) }) as MemorySegment)
    set(value) = key_VH.set(segment, value.segment)

actual var OSSL_PARAM.data_type: UInt
    get() = (data_type_VH.get(segment.also { println(it) }) as Int).toUInt()
    set(value) = data_type_VH.set(segment, value.toInt())

actual var OSSL_PARAM.data: CPointer<out CPointed>?
    get() = CPointer(data_VH.get(segment) as MemorySegment)
    set(value) = data_VH.set(segment, value.segment)

actual var OSSL_PARAM.data_size: ULong
    get() = (data_size_VH.get(segment) as Long).toULong()
    set(value) = data_size_VH.set(segment, value.toLong())

actual var OSSL_PARAM.return_size: ULong
    get() = (return_size_VH.get(segment) as Long).toULong()
    set(value) = return_size_VH.set(segment, value.toLong())

private val OSSL_PARAM_construct_utf8_string_MH: MethodHandle = FFI.methodHandle(
    name = "OSSL_PARAM_construct_utf8_string",
    result = layout,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: ULong,
): CValue<OSSL_PARAM> = cValue(
    OSSL_PARAM_Type,
    OSSL_PARAM_construct_utf8_string_MH.invokeExact(
        FFI.autoSegmentAllocator,
        key.segment,
        buf.segment,
        bsize.toLong()
    ) as MemorySegment
)

private val OSSL_PARAM_construct_end_MH: MethodHandle = FFI.methodHandle(
    name = "OSSL_PARAM_construct_end",
    result = layout
)

actual fun OSSL_PARAM_construct_end(): CValue<OSSL_PARAM> = cValue(
    OSSL_PARAM_Type,
    OSSL_PARAM_construct_end_MH.invokeExact(
        FFI.autoSegmentAllocator
    ) as MemorySegment
)
