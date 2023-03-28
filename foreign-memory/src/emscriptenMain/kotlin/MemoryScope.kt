package dev.whyoleg.foreign.memory

public actual abstract class MemoryScope {
    @ForeignMemoryApi
    public actual abstract fun allocateMemory(layout: MemoryLayout): MemorySegment

    public actual class Closeable
    @ForeignMemoryApi
    internal constructor(
        private val memory: WasmMemory
    ) : MemoryScope() {
        @ForeignMemoryApi
        private val segments = mutableListOf<MemorySegment>()

        @ForeignMemoryApi
        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            val address = memory.malloc(layout.size)
            val segment = MemorySegment(address, layout.size, memory, null)
            segments.add(segment)
            return segment
        }

        @OptIn(ForeignMemoryApi::class)
        public actual fun close() {
            segments.forEach {
                it.makeInaccessible()
                memory.free(it.address)
            }
            segments.clear()
        }
    }

    @ForeignMemoryApi
    internal class Auto(
        private val memory: WasmMemory
    ) : MemoryScope() {
        @ForeignMemoryApi
        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            val address = memory.malloc(layout.size)
            return MemorySegment(address, layout.size, memory, createCleaner { memory.free(address) })
        }
    }
}
