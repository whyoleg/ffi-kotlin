package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*

public typealias CString = CPointer<Byte>

public fun MemoryScope.string(value: String): CString {
//    pointerFor(CType.Byte)
}
