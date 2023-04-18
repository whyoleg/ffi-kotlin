package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*

@ForeignMemoryApi
public abstract class ReferenceMemoryAccessor<Ref : MemoryReference, PKT : Any>(offset: MemoryAddressSize) : MemoryAccessor<Ref>(offset) {
    final override val layout: MemoryLayout get() = MemoryLayout.Address
    protected abstract val pointedAccessor: MemoryAccessor<PKT>
    protected abstract fun wrapPointed(pointedSegment: MemorySegment): Ref

    final override fun get(segment: MemorySegment): Ref? = segment.loadPointed(offset, pointedAccessor.layout)?.let(::wrapPointed)
    final override fun set(segment: MemorySegment, value: Ref?): Unit =
        segment.storePointed(offset, pointedAccessor.layout, value?.segmentInternal)
}
