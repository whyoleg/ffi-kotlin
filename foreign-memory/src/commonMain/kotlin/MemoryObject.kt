package dev.whyoleg.foreign.memory

// TODO: decide on naming
public expect class MemoryObject {
    public val autoScope: MemoryScope
    public fun createScope(): MemoryScope.Closeable

    public companion object {
        public val Default: MemoryObject
    }
}
