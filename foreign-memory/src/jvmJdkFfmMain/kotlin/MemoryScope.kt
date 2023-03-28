package dev.whyoleg.foreign.memory

public actual abstract class MemoryScope {
    @ForeignMemoryApi
    public actual abstract fun allocateMemory(layout: MemoryLayout): MemorySegment

    public actual class Closeable actual constructor() : MemoryScope() {
        @ForeignMemoryApi
        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            TODO("Not yet implemented")
        }

        public actual fun close() {
            TODO("Not yet implemented")
        }
    }

    public actual object Auto : MemoryScope() {
        @ForeignMemoryApi
        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            TODO("Not yet implemented")
        }
    }
}
