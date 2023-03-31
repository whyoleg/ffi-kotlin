package dev.whyoleg.foreign.memory

import java.lang.foreign.*

@OptIn(ForeignMemoryApi::class)
public actual class MemoryObject private constructor() {
    public actual val autoAllocator: MemoryAllocator = MemoryAllocator { size, alignment ->
        SegmentScope.auto().alloc(size, alignment)
    }

    public actual fun createScope(): MemoryScope = Scope()

    @ForeignMemoryApi
    public actual fun unsafeMemory(address: MemoryAddress, layout: MemoryLayout): MemorySegment? =
        MemorySegment.fromAddress(address, layout)

    public actual companion object {
        public actual val Default: MemoryObject = MemoryObject()
    }

    private class Scope : MemoryScope {
        //TODO: confined vs shared
        private val arena = Arena.openShared()

        override fun allocateMemory(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
            return arena.scope().alloc(size, alignment)
        }

        override fun close() = arena.close()
    }
}

@ForeignMemoryApi
private fun SegmentScope.alloc(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
    return MemorySegment(java.lang.foreign.MemorySegment.allocateNative(size, alignment, this))
}
