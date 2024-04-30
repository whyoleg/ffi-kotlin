@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*
import kotlin.jvm.*

@JvmInline
public value class CString
@PublishedApi internal constructor(
    @PublishedApi internal val memoryBlock: MemoryBlock,
)

// TODO: may be introduce `CString` value class to be able to work better with unbounded and not
//public typealias CString = CPointer<Byte>

// TODO: design String API to be consistent with other things
public inline fun MemoryScope.cPointerFrom(value: String): CString = TODO()

public inline fun CString.toKString(): String = TODO()
////TODO: is it really a good name for function?
////TODO: better API for strings is needed...
//@OptIn(ForeignMemoryApi::class)
//public fun CString.toKString(
//    maxLength: Int = Int.MAX_VALUE, // if f.e. maxLength=100, but there is no '\0' - TODO: fail or return truncated result?
//    unsafe: Boolean = false
//): String = blockInternalC.loadString(memoryAddressSizeZero(), unsafe)
