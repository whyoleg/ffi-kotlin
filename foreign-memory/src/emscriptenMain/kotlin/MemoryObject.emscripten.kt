package dev.whyoleg.foreign.memory

@OptIn(ForeignMemoryApi::class)
public actual class MemoryObject
@ForeignMemoryApi
constructor(
    private val memory: WasmMemory
) {
    public actual val autoAllocator: MemoryAllocator = MemoryAllocator { size, _ ->
        val address = memory.malloc(size)
        MemorySegment(address, size, memory, createCleaner { memory.free(address) })
    }

    public actual fun createScope(): MemoryScope = Scope()

    @ForeignMemoryApi
    public actual fun unsafeMemory(address: MemoryAddress, layout: MemoryLayout): MemorySegment? =
        MemorySegment.fromAddress(address, layout, memory)

    public actual companion object {
        public actual val Default: MemoryObject = MemoryObject(WasmMemory.default())
    }

    private inner class Scope : MemoryScope {
        private val segments = mutableListOf<MemorySegment>()

        override fun allocateMemory(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
            val address = memory.malloc(size)
            val segment = MemorySegment(address, size, memory, null)
            segments.add(segment)
            return segment
        }

        override fun close() {
            segments.forEach {
                it.makeInaccessible()
                memory.free(it.address)
            }
            segments.clear()
        }
    }
}
