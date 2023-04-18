@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.invoke.*
import dev.whyoleg.foreign.platform.*
import java.lang.foreign.*
import java.lang.invoke.*

//TODO: may be somehow sync this with common impl?
internal val OSSL_PARAM_layout = MemoryLayout.structLayout(
    ValueLayout.ADDRESS.withName("key"), //8
    ValueLayout.JAVA_INT.withName("data_type"), //4
    MemoryLayout.paddingLayout(32), //TODO: how this works ? :) //4
    ValueLayout.ADDRESS.withName("data"), //8
    ValueLayout.JAVA_LONG.withName("data_size"), //8
    ValueLayout.JAVA_LONG.withName("return_size"), //8
).withName("ossl_param_st")  //TODO: or OSSL_PARAM?

private val OSSL_PARAM_construct_utf8_string_MH: MethodHandle = FFI.methodHandle(
    name = "OSSL_PARAM_construct_utf8_string",
    result = OSSL_PARAM_layout,
    args = arrayOf(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG)
)

actual fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformUInt,
    scope: ForeignCScope
): OSSL_PARAM = scope.unsafe {
    val address = OSSL_PARAM_construct_utf8_string_MH.invokeExact(
        arena.allocator,
        key.address,
        buf.address,
        bsize.toLong()
    ) as MemorySegment
    CGrouped(OSSL_PARAM, address)
}

private val OSSL_PARAM_construct_end_MH: MethodHandle = FFI.methodHandle(
    name = "OSSL_PARAM_construct_end",
    result = OSSL_PARAM_layout
)

actual fun OSSL_PARAM_construct_end(
    scope: ForeignCScope
): OSSL_PARAM = scope.unsafe {
    val address = OSSL_PARAM_construct_end_MH.invokeExact(arena.allocator) as MemorySegment
    CGrouped(OSSL_PARAM, address)
}
