package dev.whyoleg.ffi

internal expect object FFI {
    fun malloc(size: Int): Int
    fun free(pointer: Int)

    fun getByte(pointer: Int): Byte
    fun setByte(pointer: Int, value: Byte)

    fun getInt(pointer: Int): Int
    fun setInt(pointer: Int, value: Int)
}
