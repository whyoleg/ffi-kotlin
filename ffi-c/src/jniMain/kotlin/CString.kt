package dev.whyoleg.ffi.c

public actual fun CString.toKString(): String = JNI.getStringFromPointer(this.memory.pointer.value)!!

public fun CString(pointer: NativePointer): CString? = CPointer(pointer, ByteVariableType)
