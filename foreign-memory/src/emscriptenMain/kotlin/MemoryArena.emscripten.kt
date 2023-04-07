package dev.whyoleg.foreign.memory

@Suppress("ACTUAL_WITHOUT_EXPECT")
@ForeignMemoryApi
public actual typealias MemoryAllocator = WasmMemory

@ForeignMemoryApi
internal sealed class MemoryArenaImpl private constructor(
    override val allocator: MemoryAllocator
) : MemoryArena {
    override fun allocate(layout: MemoryLayout): MemorySegment = TODO()
    override fun allocateArray(elementLayout: MemoryLayout, elementsCount: Int): MemorySegment = TODO()
    override fun allocateString(value: String): MemorySegment = TODO()

    override fun wrap(address: MemoryAddress, layout: MemoryLayout): MemorySegment? = TODO()

    class Shared(memory: WasmMemory) : MemoryArenaImpl(memory) {
        private val segments = mutableListOf<MemorySegment>()
        private val cleaner = createCleaner(FreeAction(memory, segments))

        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
            val address = allocator.malloc(size)
            val segment = MemorySegment(address, size, allocator, null)
            segments.add(segment)
            return segment
        }

        override fun close() = cleaner.clean()

        private class FreeAction(private val memory: WasmMemory, private val segments: MutableList<MemorySegment>) : CleanupAction() {
            override fun run() {
                segments.forEach {
                    it.makeInaccessible()
                    memory.free(it.address)
                }
                segments.clear()
            }
        }
    }

    class Implicit(memory: WasmMemory) : MemoryArenaImpl(memory) {
        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment {
            val address = allocator.malloc(size)
            val cleaner = createCleaner(FreeAction(allocator, address))
            return MemorySegment(address, size, allocator, cleaner)
        }

        override fun close() {
            //no-op
        }

        private class FreeAction(private val memory: WasmMemory, private val address: MemoryAddress) : CleanupAction() {
            override fun run(): Unit = memory.free(address)
        }
    }
}
