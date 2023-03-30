package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*

@ForeignMemoryApi
internal object VoidMemoryAccessor : MemoryAccessor<Unit>(memoryAddressSizeZero()) {
    override val layout: MemoryLayout get() = MemoryLayout.Void
    override fun get(segment: MemorySegment): Unit = Unit
    override fun set(segment: MemorySegment, value: Unit?) {}
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<Unit> = this
}