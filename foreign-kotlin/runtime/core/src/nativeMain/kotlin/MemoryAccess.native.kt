package dev.whyoleg.foreign

import kotlinx.cinterop.*

@Suppress("FunctionName")
public actual fun AutoMemoryAccess(): MemoryAccess = GlobalMemoryAccess

@Suppress("FunctionName")
public actual fun GlobalMemoryAccess(): MemoryAccess = GlobalMemoryAccess

private object GlobalMemoryAccess : InternalMemoryAccess {
    override fun createArena(): MemoryArena = NativeMemoryArena()
}

@OptIn(ExperimentalForeignApi::class)
private class NativeMemoryArena : InternalMemoryArena {
    private val arena = Arena()

    override fun createArena(): MemoryArena {
        // TODO: parent-child relationship
        return NativeMemoryArena()
    }

    override fun close() {
        arena.clear()
    }

    override fun Unsafe.allocate(size: MemorySizeInt, alignment: MemorySizeInt): MemoryBlock {
        return NativeMemoryBlock(arena.alloc(size, alignment.toInt()), size).also {
            arena.defer { it.makeInaccessible() }
        }
    }

    override fun Unsafe.allocate(layout: MemoryLayout): MemoryBlock {
        return allocate(layout.size, layout.alignment)
    }

    override fun Unsafe.allocateArray(elementLayout: MemoryLayout, elementsCount: Int): MemoryBlock {
        TODO("Not yet implemented")
    }

    override fun Unsafe.allocateFromString(value: String): MemoryBlock {
        TODO("Not yet implemented")
    }
}
