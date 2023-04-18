package dev.whyoleg.foreign.memory

//TODO: come up with better name...
//TODO: looks like it could be just abstract class?
public expect class ForeignMemory {
    public companion object {
        public val Default: ForeignMemory
    }

    // close does nothing, and memory is deallocated automatically
    @ForeignMemoryApi
    public val implicit: MemoryArena

    @ForeignMemoryApi
    public fun createArena(): MemoryArena
}
