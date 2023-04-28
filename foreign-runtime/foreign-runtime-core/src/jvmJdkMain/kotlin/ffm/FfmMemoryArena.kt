package dev.whyoleg.foreign.ffm

import dev.whyoleg.foreign.memory.*
import java.lang.foreign.*
import java.lang.ref.*

@ForeignMemoryApi
internal sealed class FfmMemoryArena : MemoryArena() {
    abstract val allocator: SegmentAllocator
    protected abstract val scope: SegmentScope
    override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemoryBlock {
        return FfmMemoryBlock(allocator.allocate(size, alignment))
    }

    override fun allocateString(value: String): MemoryBlock {
        return FfmMemoryBlock(allocator.allocateUtf8String(value))
    }

    override fun wrap(address: MemoryAddressSize, layout: MemoryBlockLayout): MemoryBlock? {
        return FfmMemoryBlock.fromAddress(address, layout.size, scope)
    }

    class Shared : FfmMemoryArena() {
        private val arena = Arena.openShared()
        private val actions = mutableListOf<() -> Unit>()
        private val cleaner = CLEANER.register(this, CleanupAction(arena, actions))
        override val scope: SegmentScope get() = arena.scope()
        override val allocator: SegmentAllocator get() = arena
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

    object Implicit : FfmMemoryArena() {
        override val scope: SegmentScope get() = SegmentScope.auto()
        override val allocator: SegmentAllocator get() = SegmentAllocator.nativeAllocator(scope)

        override fun close() {
            TODO("should not be called on implicit scope")
        }

        override fun invokeOnClose(block: () -> Unit) {
            TODO("should not be called on implicit scope")
        }
    }
}

private val CLEANER = Cleaner.create()
