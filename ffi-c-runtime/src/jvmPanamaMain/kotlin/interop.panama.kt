package dev.whyoleg.ffi.c

import java.lang.foreign.*

public val CPointer<*>?.nativeAddress: MemorySegment get() = this?.pointer?.segment ?: MemorySegment.NULL

public val CPointed?.nativeAddress: MemorySegment get() = this?.memory?.pointer?.segment ?: MemorySegment.NULL

public fun <T : CPointed> nativeCPointer(type: CPointedType<T>, nativeAddress: MemorySegment): CPointer<T>? =
    CPointer.of(type, NativePointer(nativeAddress))

public fun nativeCString(nativeAddress: MemorySegment): CString? = nativeCPointer(ByteVariableType, nativeAddress)

public fun <T : CVariable> nativeCValue(type: CVariableType<T>, block: (allocator: SegmentAllocator) -> MemorySegment): CValue<T> {
    //single use allocator
    val segment = block(SegmentAllocator.nativeAllocator(SegmentScope.auto()))
    return CValueImpl(NativeMemory(segment), type)
}
