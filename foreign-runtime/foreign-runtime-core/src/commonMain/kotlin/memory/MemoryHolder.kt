@file:OptIn(ForeignMemoryApi::class)

package dev.whyoleg.foreign.memory

public sealed class MemoryHolder(protected val block: MemoryBlock) {
    public val isAccessible: Boolean get() = block.isAccessible

    @PublishedApi
    @ForeignMemoryApi
    internal val blockInternal: MemoryBlock get() = block
}

public abstract class MemoryReference @ForeignMemoryApi constructor(block: MemoryBlock) : MemoryHolder(block)

//TODO: other name?
public abstract class MemoryValue @ForeignMemoryApi constructor(block: MemoryBlock) : MemoryHolder(block)

//for opaque support
public abstract class EmptyMemoryValue @ForeignMemoryApi constructor()
