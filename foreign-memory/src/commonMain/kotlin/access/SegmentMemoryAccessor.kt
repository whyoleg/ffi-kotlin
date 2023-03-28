package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*

@ForeignMemoryApi
public abstract class SegmentMemoryAccessor<KT : MemoryHolder>(offset: MemoryAddressSize) : MemoryAccessor<KT>(offset) {
    protected abstract fun wrap(segment: MemorySegment): KT

    final override fun get(segment: MemorySegment): KT =
        wrap(segment.loadSegment(offset, layout))

    final override fun set(segment: MemorySegment, value: KT?): Unit =
        segment.storeSegment(offset, layout, value?.segment ?: MemorySegment.Empty)
}
