package dev.whyoleg.foreign.memory

import kotlinx.cinterop.*
import kotlinx.cinterop.NativePtr
import kotlin.native.internal.*

@ForeignMemoryApi
internal sealed class NativeMemoryArena : MemoryArena() {
    protected abstract val placement: NativePlacement
    protected fun alloc(size: MemoryAddressSize, alignment: MemoryAddressSize): NativePtr =
        placement.alloc(size, alignment.toInt()).rawPtr

    override fun wrap(address: MemoryAddressSize, layout: MemoryBlockLayout): MemoryBlock? {
        //TODO: configure cleanup!!!
        return NativeMemoryBlock.fromAddress(nativeNullPtr + address, layout)
    }

    class Shared : NativeMemoryArena() {
        private val arena = Arena()
        private val cleaner = createCleaner(arena, Arena::clear)
        override val placement: NativePlacement get() = arena

        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemoryBlock {
            val ptr = alloc(size, alignment)
            val segment = NativeMemoryBlock(ptr, size, null)
            arena.defer { segment.makeInaccessible() }
            return segment
        }

        override fun close(): Unit = arena.clear()

        override fun invokeOnClose(block: () -> Unit) {
            arena.defer(block)
        }
    }

    object Implicit : NativeMemoryArena() {
        override val placement: NativePlacement get() = nativeHeap
        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemoryBlock {
            val ptr = alloc(size, alignment)
            val cleaner = createCleaner(ptr) { nativeHeap.free(it) }
            return NativeMemoryBlock(ptr, size, cleaner)
        }

        override fun close() {
            TODO("should not be called on implicit scope")
        }

        override fun invokeOnClose(block: () -> Unit) {
            TODO("should not be called on implicit scope")
        }
    }
}
