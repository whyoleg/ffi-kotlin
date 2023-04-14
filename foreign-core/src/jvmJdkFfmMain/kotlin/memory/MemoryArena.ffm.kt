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
        return MemorySegment(allocator.allocate(size, alignment))
    }

    override fun allocateString(value: String): MemorySegment {
        return MemorySegment(allocator.allocateUtf8String(value))
    }

    override fun wrap(address: MemoryAddress, layout: MemoryLayout): MemorySegment? {
        return MemorySegment.fromAddress(address, layout.size, scope)
    }

    class Shared : MemoryArenaImpl() {
        private val arena = Arena.openShared()
        private val actions = mutableListOf<() -> Unit>()
        private val cleaner = CLEANER.register(this, CleanupAction(arena, actions))
        override val scope: SegmentScope get() = arena.scope()
        override val allocator: MemoryAllocator get() = arena
        override fun close(): Unit = cleaner.clean()
        override fun invokeOnClose(block: () -> Unit) {
            actions.add(block)
        }

        private class CleanupAction(
            private val arena: Arena,
            private val actions: MutableList<() -> Unit>
        ) : Runnable {
            override fun run() {
                actions.forEach { it.runCatching { invoke() } }
                actions.clear()
                arena.close()
            }
        }
    }

    object Implicit : MemoryArenaImpl() {
        override val scope: SegmentScope get() = SegmentScope.auto()
        override val allocator: MemoryAllocator get() = SegmentAllocator.nativeAllocator(scope)

        override fun close() {
            TODO("should not be called on implicit scope")
        }

        override fun invokeOnClose(block: () -> Unit) {
            TODO("should not be called on implicit scope")
        }
    }
}

private val CLEANER = Cleaner.create()
