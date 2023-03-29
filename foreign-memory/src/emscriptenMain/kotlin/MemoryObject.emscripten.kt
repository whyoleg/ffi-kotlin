package dev.whyoleg.foreign.memory

@OptIn(ForeignMemoryApi::class)
public actual class MemoryObject
@ForeignMemoryApi
constructor(
    private val memory: WasmMemory
) {
    public actual val autoAllocator: MemoryAllocator = MemoryAllocator { layout ->
        val address = memory.malloc(layout.size)
        MemorySegment(address, layout.size, memory, createCleaner { memory.free(address) })
    }

    public actual fun createScope(): MemoryScope = Scope()

    public actual companion object {
        public actual val Default: MemoryObject = MemoryObject(WasmMemory.default())
    }

    private inner class Scope : MemoryScope {
        private val segments = mutableListOf<MemorySegment>()

        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            val address = memory.malloc(layout.size)
            val segment = MemorySegment(address, layout.size, memory, null)
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
