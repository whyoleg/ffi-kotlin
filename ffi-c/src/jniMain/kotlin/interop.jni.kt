package dev.whyoleg.ffi.c

import java.nio.*

public val CPointer<*>?.nativeAddress: Long get() = this?.pointer?.value ?: 0

public fun <T : CPointed> nativeCPointer(type: CPointedType<T>, nativeAddress: Long): CPointer<T>? =
    CPointer.of(type, NativePointer(nativeAddress))

public fun nativeCString(nativeAddress: Long): CString? = nativeCPointer(ByteVariableType, nativeAddress)

public fun <T : CVariable> nativeCValue(type: CVariableType<T>, block: (nativeAddress: Long) -> Unit): CValue<T> {
    val memory = NativeMemory(ByteBuffer.allocateDirect(type.layout.byteSize))
    block(memory.pointer.value)
    return CValueImpl(memory, type)
}
