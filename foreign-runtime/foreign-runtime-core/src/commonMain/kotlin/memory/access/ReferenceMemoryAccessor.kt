package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*

@ForeignMemoryApi
public abstract class ReferenceMemoryAccessor<Ref : MemoryReference, PKT : Any>(offset: MemoryAddressSize) : MemoryAccessor<Ref>(offset) {
    final override val layout: MemoryBlockLayout get() = MemoryBlockLayout.Address
    protected abstract val pointedAccessor: MemoryAccessor<PKT>
    protected abstract fun wrapPointed(pointedSegment: MemoryBlock): Ref

    final override fun get(block: MemoryBlock): Ref? = block.loadPointed(offset, pointedAccessor.layout)?.let(::wrapPointed)
    final override fun set(block: MemoryBlock, value: Ref?): Unit =
        block.storePointed(offset, pointedAccessor.layout, value?.blockInternal)
}
