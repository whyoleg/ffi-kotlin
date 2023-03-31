package dev.whyoleg.foreign.memory

import kotlinx.cinterop.*
import kotlinx.cinterop.NativePtr
import kotlin.native.internal.*

@OptIn(ForeignMemoryApi::class)
public actual class MemoryObject private constructor() {
    public actual val autoAllocator: MemoryAllocator = MemoryAllocator { size, alignment ->
        val ptr = nativeHeap.alloc(size, alignment)
        val cleaner = createCleaner(ptr) { nativeHeap.free(it) }
        MemorySegment(ptr, size, cleaner)
    }

    public actual fun createScope(): MemoryScope = Scope()

    @ForeignMemoryApi
    public actual fun unsafeMemory(address: MemoryAddress, layout: MemoryLayout): MemorySegment? =
        MemorySegment.fromAddress(address, layout)

    public actual companion object {
        public actual val Default: MemoryObject = MemoryObject()
    }

    private class Scope : MemoryScope {
        private val arena = Arena()
        override fun allocateMemory(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
            val ptr = arena.alloc(size, alignment)
            val segment = MemorySegment(ptr, size, null)
            arena.defer { segment.makeInaccessible() }
            return segment
        }

        override fun close() = arena.clear()
    }
}

@ForeignMemoryApi
private fun NativePlacement.alloc(size: MemoryAddressSize, alignment: MemoryAddressSize): NativePtr = alloc(size, alignment.toInt()).rawPtr
