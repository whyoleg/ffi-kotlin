package dev.whyoleg.foreign.memory

import java.lang.foreign.*
import java.lang.foreign.MemorySegment as JMemorySegment

public actual abstract class MemoryScope {
    protected abstract val scope: SegmentScope

    @ForeignMemoryApi
    public actual fun allocateMemory(layout: MemoryLayout): MemorySegment {
        return MemorySegment(JMemorySegment.allocateNative(layout.size, layout.alignment, scope))
    }

    public actual abstract class Closeable internal constructor(
        private val arena: Arena
    ) : MemoryScope() {
        final override val scope: SegmentScope get() = arena.scope()
        public actual fun close(): Unit = arena.close()
    }

    internal object Auto : MemoryScope() {
        override val scope: SegmentScope get() = SegmentScope.auto()
    }

    //TODO: confined vs shared
    internal class Impl : Closeable(Arena.openShared())
}
