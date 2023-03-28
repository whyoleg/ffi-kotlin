package dev.whyoleg.foreign.memory

// TODO: decide on naming
public expect abstract class MemoryObject {
    public val autoScope: MemoryScope
    public fun createScope(): MemoryScope.Closeable

    //TODO: how to work with default?
    public object Default : MemoryObject

    public companion object
}
