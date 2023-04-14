package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*

public typealias CString = UnsafeCPointer<Byte>

//TODO: is it really a good name for function?
@OptIn(ForeignMemoryApi::class)
public fun CString.toKString(): String = segmentInternal2.loadString(memoryAddressSizeZero())

@OptIn(ForeignMemoryApi::class)
public fun ForeignCScope.cString(value: String): CString = unsafe {
    return CPointer(CType.Byte, arena.allocateString(value))
}
