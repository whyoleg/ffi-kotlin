package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.internal.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
@ForeignMemoryApi
public actual typealias MemoryAllocator = WasmMemory

@ForeignMemoryApi
internal sealed class MemoryArenaImpl private constructor(
    override val allocator: MemoryAllocator
) : MemoryArena {
    override fun wrap(address: MemoryAddress, layout: MemoryLayout): MemorySegment? {
        return MemorySegment.fromAddress(address, layout, allocator)
    }

    class Shared(memory: WasmMemory) : MemoryArenaImpl(memory) {
        private val actions = mutableListOf<() -> Unit>()
        private val cleaner = createCleaner(FreeAction(actions))

        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
            val address = allocator.malloc(size)
            val segment = MemorySegment(address, size, allocator, null)

            //TODO: will it work correctly?
            val memory = allocator
            actions.add {
                segment.makeInaccessible()
                memory.free(segment.address)
            }
            return segment
        }

        override fun close() {
            cleaner.clean()
        }

        override fun invokeOnClose(block: () -> Unit) {
            actions.add(block)
        }

        private class FreeAction(private val actions: MutableList<() -> Unit>) : CleanupAction() {
            override fun run() {
                actions.forEach { it.runCatching { invoke() } }
                actions.clear()
            }
        }
    }

    class Implicit(memory: WasmMemory) : MemoryArenaImpl(memory) {
        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
            val address = allocator.malloc(size)
            val cleaner = createCleaner(FreeAction(allocator, address))
            return MemorySegment(address, size, allocator, cleaner)
        }

        override fun invokeOnClose(block: () -> Unit) {
            TODO("should not be called on implicit scope")
        }

        override fun close() {
            TODO("should not be called on implicit scope")
        }

        private class FreeAction(private val memory: WasmMemory, private val address: MemoryAddress) : CleanupAction() {
            override fun run(): Unit = memory.free(address)
        }
    }
}
