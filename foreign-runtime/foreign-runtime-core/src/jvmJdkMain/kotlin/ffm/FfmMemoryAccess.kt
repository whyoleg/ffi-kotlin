package dev.whyoleg.foreign.ffm

import dev.whyoleg.foreign.memory.*

@OptIn(ForeignMemoryApi::class)
internal object FfmMemoryAccess : MemoryAccess() {
    override val implicit: MemoryArena get() = FfmMemoryArena.Implicit
    override fun createArena(): MemoryArena = FfmMemoryArena.Shared()
}
