package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*

@ForeignMemoryApi
public abstract class PointedMemoryAccessor<Ref : MemoryHolder, PKT : Any>(offset: MemoryAddressSize) : MemoryAccessor<Ref>(offset) {
    override val layout: MemoryLayout get() = MemoryLayout.Address
    protected abstract val pointedAccessor: MemoryAccessor<PKT>
    protected abstract fun wrap(pointedSegment: MemorySegment): Ref

    final override fun get(segment: MemorySegment): Ref? = segment.loadPointed(offset, pointedAccessor.layout)?.let(::wrap)
    final override fun set(segment: MemorySegment, value: Ref?): Unit = segment.storePointed(offset, pointedAccessor.layout, value?.segment)
}
