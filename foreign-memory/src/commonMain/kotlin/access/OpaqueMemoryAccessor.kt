package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*

@ForeignMemoryApi
public class OpaqueMemoryAccessor<KT : Any>(
    private val instance: KT
) : MemoryAccessor<KT>(memoryAddressSizeZero()) {
    override val layout: MemoryLayout get() = MemoryLayout.Void
    override fun get(segment: MemorySegment): KT = instance
    override fun set(segment: MemorySegment, value: KT?) {}
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<KT> = this
}
