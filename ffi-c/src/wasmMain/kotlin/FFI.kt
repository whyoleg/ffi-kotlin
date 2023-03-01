@file:Suppress("FunctionName")

package dev.whyoleg.ffi.c

internal actual object FFI {
    actual fun malloc(size: Int): Int = _malloc(size)
    actual fun free(pointer: Int): Unit = _free(pointer)
    actual fun getByte(pointer: Int): Byte = _getU8(pointer)
    actual fun setByte(pointer: Int, value: Byte): Unit = _setU8(pointer, value)
    actual fun getInt(pointer: Int): Int = _getU32(pointer)
    actual fun setInt(pointer: Int, value: Int) = _setU32(pointer, value)
}

//TODO: those are really super dependent - need to check on how to provide it better
@JsFun("(index) => imports.Module.HEAPU8[index]")
private external fun _getU8(index: Int): Byte

@JsFun("(index, value) => imports.Module.HEAPU8[index] = value")
private external fun _setU8(index: Int, value: Byte)

@JsFun("(index) => imports.Module.HEAPU32[index]")
private external fun _getU32(index: Int): Int

@JsFun("(index, value) => imports.Module.HEAPU32[index] = value")
private external fun _setU32(index: Int, value: Int)

@JsFun("(size) => imports.Module.asm.malloc(size)")
private external fun _malloc(size: Int): Int

@JsFun("(pointer) => imports.Module.asm.free(pointer)")
private external fun _free(pointer: Int)
