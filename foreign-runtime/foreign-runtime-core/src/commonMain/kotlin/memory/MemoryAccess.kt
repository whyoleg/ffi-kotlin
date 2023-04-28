package dev.whyoleg.foreign.memory

@ForeignMemoryApi
public abstract class MemoryAccess internal constructor() {
    // close throws, and memory is deallocated automatically
    public abstract val implicit: MemoryArena
    public abstract fun createArena(): MemoryArena

    public companion object {
        public val Auto: MemoryAccess = createAutoMemoryAccess()
    }
}
