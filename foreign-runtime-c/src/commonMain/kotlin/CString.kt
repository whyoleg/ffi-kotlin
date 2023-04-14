package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*

public typealias CString = CPointer<Byte>

//TODO: is it really a good name for function?
@OptIn(ForeignMemoryApi::class)
public fun CString.toKString(
    maxLength: Int = Int.MAX_VALUE // if f.e. maxLength=100, but there is no '\0' - TODO: fail or return truncated result?
): String = segmentInternal2.loadString(memoryAddressSizeZero())

@OptIn(ForeignMemoryApi::class)
public fun ForeignCScope.cString(value: String): CString = unsafe {
    return CPointer(CType.Byte, arena.allocateString(value))
}
