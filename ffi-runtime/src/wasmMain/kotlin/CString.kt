package dev.whyoleg.ffi

public actual fun CString.toKString(): String {
    var length = 0
    while (true) {
        val b = this.memory.loadByte(length)
        if (b == 0.toByte()) break
        length++
    }
    return ByteArray(length) { this.memory.loadByte(it) }.decodeToString()
}

public fun CString(pointer: NativePointer): CString? = CPointer(pointer, ByteVariableType)
