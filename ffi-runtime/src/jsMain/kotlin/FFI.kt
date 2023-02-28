package dev.whyoleg.ffi

import org.khronos.webgl.*

internal actual object FFI {
    actual fun malloc(size: Int): Int = ffi.asm.malloc(size)
    actual fun free(pointer: Int) = ffi.asm.free(pointer)
    actual fun getByte(pointer: Int): Byte = ffi.HEAPU8[pointer]
    actual fun setByte(pointer: Int, value: Byte): Unit = run { ffi.HEAPU8[pointer] = value }
    actual fun getInt(pointer: Int): Int = ffi.HEAPU32[pointer]
    actual fun setInt(pointer: Int, value: Int) = run { ffi.HEAPU32[pointer] = value }
}

//TODO how to do it for work with everything???
@JsModule("ffi-libcrypto")
@JsNonModule
@JsName("Module")
private external object ffi {
    val HEAPU8: Uint8Array
    val HEAPU32: Uint32Array

    object asm {
        fun malloc(size: Int): Int
        fun free(pointer: Int)
    }
}
