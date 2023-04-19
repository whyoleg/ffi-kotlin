package dev.whyoleg.foreign.memory

import kotlinx.cinterop.*
import kotlinx.cinterop.NativePtr
import kotlin.native.internal.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
@ForeignMemoryApi
public actual typealias MemoryAllocator = NativePlacement

@ForeignMemoryApi
internal sealed class MemoryArenaImpl : MemoryArena {
    protected fun alloc(size: MemoryAddressSize, alignment: MemoryAddressSize): NativePtr =
        allocator.alloc(size, alignment.toInt()).rawPtr

    override fun wrap(address: MemoryAddress, layout: MemoryLayout): MemorySegment? {
        //TODO: configure cleanup!!!
        return MemorySegment.fromAddress(nativeNullPtr + address, layout)
    }

    class Shared : MemoryArenaImpl() {
        private val arena = Arena()
        private val cleaner = createCleaner(arena, Arena::clear)
        override val allocator: MemoryAllocator get() = arena

        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
            val ptr = alloc(size, alignment)
            val segment = MemorySegment(ptr, size, null)
            arena.defer { segment.makeInaccessible() }
            return segment
        }

        override fun close(): Unit = arena.clear()

        override fun invokeOnClose(block: () -> Unit) {
            arena.defer(block)
        }
    }

    object Implicit : MemoryArenaImpl() {
        override val allocator: MemoryAllocator get() = nativeHeap
        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
            val ptr = alloc(size, alignment)
            val cleaner = createCleaner(ptr) { nativeHeap.free(it) }
            return MemorySegment(ptr, size, cleaner)
        }

        override fun close() {
            TODO("should not be called on implicit scope")
        }

        override fun invokeOnClose(block: () -> Unit) {
            TODO("should not be called on implicit scope")
        }
    }
}
