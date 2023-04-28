package dev.whyoleg.foreign.nio

import dev.whyoleg.foreign.memory.*

@OptIn(ForeignMemoryApi::class)
internal object ByteBufferMemoryAccess : MemoryAccess() {
    override val implicit: MemoryArena get() = ByteBufferMemoryArena.Implicit
    override fun createArena(): MemoryArena = ByteBufferMemoryArena.Shared()
}
