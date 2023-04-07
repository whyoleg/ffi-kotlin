package dev.whyoleg.foreign.memory

import java.lang.foreign.*
import java.lang.ref.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
@ForeignMemoryApi
public actual typealias MemoryAllocator = SegmentAllocator

@ForeignMemoryApi
internal sealed class MemoryArenaImpl : MemoryArena {
    protected abstract val scope: SegmentScope
    override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
        return MemorySegment(java.lang.foreign.MemorySegment.allocateNative(size, alignment, scope))
    }

    override fun allocateArray(elementLayout: MemoryLayout, elementsCount: Int): MemorySegment = TODO()
    override fun allocateString(value: String): MemorySegment = TODO()

    override fun wrap(address: MemoryAddress, layout: MemoryLayout): MemorySegment? = TODO()

    class Shared : MemoryArenaImpl() {
        private val arena = Arena.openShared()
        private val cleaner = CLEANER.register(this, arena::close)
        override val scope: SegmentScope get() = arena.scope()
        override val allocator: MemoryAllocator get() = arena
        override fun close(): Unit = cleaner.clean()
    }

    object Implicit : MemoryArenaImpl() {
        override val scope: SegmentScope get() = SegmentScope.auto()
        override val allocator: MemoryAllocator get() = SegmentAllocator.nativeAllocator(scope)
        override fun close() {
            //no-op
        }
    }
}

private val CLEANER = Cleaner.create()
