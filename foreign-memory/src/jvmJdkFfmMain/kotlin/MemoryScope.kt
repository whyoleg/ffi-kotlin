package dev.whyoleg.foreign.memory

import java.lang.foreign.*
import java.lang.foreign.MemorySegment as JMemorySegment

public actual abstract class MemoryScope {
    @ForeignMemoryApi
    protected fun SegmentScope.alloc(layout: MemoryLayout): MemorySegment {
        return MemorySegment(JMemorySegment.allocateNative(layout.size, layout.alignment, this))
    }

    @ForeignMemoryApi
    public actual abstract fun allocateMemory(layout: MemoryLayout): MemorySegment

    public actual class Closeable internal constructor() : MemoryScope() {
        //TODO: confined vs shared
        private val arena = Arena.openShared()

        @ForeignMemoryApi
        override fun allocateMemory(layout: MemoryLayout): MemorySegment = arena.scope().alloc(layout)
        public actual fun close(): Unit = arena.close()
    }

    internal object Auto : MemoryScope() {
        @ForeignMemoryApi
        override fun allocateMemory(layout: MemoryLayout): MemorySegment = SegmentScope.auto().alloc(layout)
    }
}
