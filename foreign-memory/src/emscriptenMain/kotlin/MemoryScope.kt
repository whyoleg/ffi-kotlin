package dev.whyoleg.foreign.memory

public actual abstract class MemoryScope {
    @ForeignMemoryApi
    public actual fun allocateMemory(layout: MemoryLayout): MemorySegment = TODO()

    public actual abstract class Closeable : MemoryScope() {
        public actual fun close() {
            TODO("Not yet implemented")
        }
    }
}
