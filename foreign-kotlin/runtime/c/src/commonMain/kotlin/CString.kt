@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*
import kotlin.jvm.*

@JvmInline
public value class CString
@PublishedApi internal constructor(
    @PublishedApi internal val memoryBlock: MemoryBlock,
) {
    public val size: Int get() = memoryBlock.size.toInt() // TODO
    public val pointer: CPointer<Byte> get() = CPointer(memoryBlock)

    //TODO: is it really a good name for function?
    //TODO: better API for strings is needed...
    // if f.e maxLength=100, but there is no '\0' - TODO: fail or return truncated result?
    public fun toKString(
        maxLength: Int = Int.MAX_VALUE,
        /*unsafe: Boolean = false*/
    ): String = TODO()
}

// TODO: design String API to be consistent with other things
public inline fun MemoryScope.allocateCString(value: String): CString = TODO()
