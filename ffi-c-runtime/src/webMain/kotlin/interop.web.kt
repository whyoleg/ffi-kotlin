package dev.whyoleg.ffi.c

public val CPointer<*>?.nativeAddress: Int get() = this?.pointer?.value ?: 0

public fun <T : CPointed> nativeCPointer(type: CPointedType<T>, nativeAddress: Int): CPointer<T>? =
    CPointer.of(type, NativePointer(nativeAddress))

public fun nativeCString(nativeAddress: Int): CString? = nativeCPointer(ByteVariableType, nativeAddress)

public fun <T : CVariable> nativeCValue(type: CVariableType<T>, block: (nativeAddress: Int) -> Unit): CValue<T> {
    val memory = NativeMemory(NativePointer(FFI.malloc(type.layout.byteSize)), type.layout.byteSize)
    block(memory.pointer.value)
    return CValueImpl(memory, type)
}
