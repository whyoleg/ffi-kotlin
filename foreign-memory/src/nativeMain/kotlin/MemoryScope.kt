package dev.whyoleg.foreign.memory

import kotlinx.cinterop.*
import kotlin.native.internal.*
import kotlin.native.internal.NativePtr

public actual abstract class MemoryScope {
    protected abstract val placement: NativePlacement
    protected abstract fun cleaner(ptr: NativePtr): Cleaner?

    @ForeignMemoryApi
    public actual fun allocateMemory(layout: MemoryLayout): MemorySegment {
        val ptr = placement.alloc(layout.size, layout.alignment.toInt()).rawPtr
        return MemorySegment(ptr, layout.size, cleaner(ptr))
    }

    public actual abstract class Closeable internal constructor(override val placement: Arena) : MemoryScope() {
        override fun cleaner(ptr: NativePtr): Cleaner? = null
        public actual fun close(): Unit = placement.clear()
    }

    internal object Auto : MemoryScope() {
        override val placement: NativePlacement get() = nativeHeap
        override fun cleaner(ptr: NativePtr): Cleaner = createCleaner(ptr, nativeHeap::free)
    }

    internal class Impl : Closeable(Arena(nativeHeap))
}
