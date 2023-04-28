package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*

@ForeignMemoryApi
internal object VoidMemoryAccessor : MemoryAccessor<Unit>(memoryAddressSizeZero()) {
    override val layout: MemoryBlockLayout get() = MemoryBlockLayout.Void
    override fun get(block: MemoryBlock): Unit = Unit
    override fun set(block: MemoryBlock, value: Unit?) {}
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<Unit> = this
}
