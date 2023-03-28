package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*

@ForeignMemoryApi
public abstract class AddressMemoryAccessor<Ref : MemoryHolder, PKT : Any>(offset: MemoryAddressSize) : MemoryAccessor<Ref>(offset) {
    override val layout: MemoryLayout get() = MemoryLayout.Address
    protected abstract val pointedAccessor: MemoryAccessor<PKT>
    protected abstract fun wrap(segment: MemorySegment): Ref

    final override fun get(segment: MemorySegment): Ref? = segment.loadAddress(offset, pointedAccessor.layout)?.let(::wrap)
    final override fun set(segment: MemorySegment, value: Ref?): Unit = segment.storeAddress(offset, pointedAccessor.layout, value?.segment)
}
