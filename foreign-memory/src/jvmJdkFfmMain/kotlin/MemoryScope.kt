package dev.whyoleg.foreign.memory

import java.lang.foreign.*
import java.lang.foreign.MemorySegment as JMemorySegment

public actual abstract class MemoryScope {
    @ForeignMemoryApi
    public actual abstract fun allocateMemory(layout: MemoryLayout): MemorySegment

    public actual class Closeable actual constructor() : MemoryScope() {
        //TODO: confined vs shared
        private val arena = Arena.openShared()

        @ForeignMemoryApi
        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            return MemorySegment(JMemorySegment.allocateNative(layout.size, layout.alignment, arena.scope()))
        }

        public actual fun close() {
            arena.close()
        }
    }

    public actual object Auto : MemoryScope() {
        @ForeignMemoryApi
        override fun allocateMemory(layout: MemoryLayout): MemorySegment {
            return MemorySegment(JMemorySegment.allocateNative(layout.size, layout.alignment, SegmentScope.auto()))
        }
    }
}
