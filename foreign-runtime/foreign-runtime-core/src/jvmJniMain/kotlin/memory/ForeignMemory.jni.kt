package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.library.*

@OptIn(ForeignMemoryApi::class)
public actual class ForeignMemory private constructor() {
    init {
        loadLibraries()
    }

    public actual companion object {
        public actual val Default: ForeignMemory = ForeignMemory()
    }

    public actual val implicit: MemoryArena get() = MemoryArenaImpl.Implicit
    public actual fun createArena(): MemoryArena = MemoryArenaImpl.Shared()
}
