@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*
import java.lang.foreign.*

private val layout = MemoryLayout.structLayout(
    ValueLayout.ADDRESS.withName("key"),
    ValueLayout.JAVA_INT.withName("data_type"),
    MemoryLayout.paddingLayout(32), //TODO: how this works ? :)
    ValueLayout.ADDRESS.withName("data"),
    ValueLayout.JAVA_LONG.withName("data_size"),
    ValueLayout.JAVA_LONG.withName("return_size"),
).withName("ossl_param_st")  //TODO: or OSSL_PARAM?

private val key_VH = layout.varHandle(MemoryLayout.PathElement.groupElement("key"))
private val data_type_VH = layout.varHandle(MemoryLayout.PathElement.groupElement("data_type"))
private val data_VH = layout.varHandle(MemoryLayout.PathElement.groupElement("data"))
private val data_size_VH = layout.varHandle(MemoryLayout.PathElement.groupElement("data_size"))
private val return_size_VH = layout.varHandle(MemoryLayout.PathElement.groupElement("return_size"))

actual object OSSL_PARAM_Type : CVariableType<OSSL_PARAM>(::OSSL_PARAM, layout)

actual class OSSL_PARAM(segment: MemorySegment) : CStructVariable(segment)

actual var OSSL_PARAM.key: CString?
    get() = CPointer(key_VH.get(segment.also { println(it) }) as MemorySegment, ::CByteVariable)
    set(value) = key_VH.set(segment, value.segment)

actual var OSSL_PARAM.data_type: CUInt
    get() = (data_type_VH.get(segment.also { println(it) }) as Int).toUInt()
    set(value) = data_type_VH.set(segment, value.toInt())

actual var OSSL_PARAM.data: CPointer<out CPointed>?
    get() = COpaquePointer(data_VH.get(segment) as MemorySegment)
    set(value) = data_VH.set(segment, value.segment)

actual var OSSL_PARAM.data_size: CULong
    get() = (data_size_VH.get(segment) as Long).toULong()
    set(value) = data_size_VH.set(segment, value.toLong())

actual var OSSL_PARAM.return_size: CULong
    get() = (return_size_VH.get(segment) as Long).toULong()
    set(value) = return_size_VH.set(segment, value.toLong())
