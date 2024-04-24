package dev.whyoleg.foreign

// TODO which access is needed?

@Suppress("FunctionName")
public actual fun AutoMemoryAccess(): MemoryAccess {
    TODO("Not yet implemented")
}

@Suppress("FunctionName")
public fun LinearMemoryAccess(): MemoryAccess {
    TODO("Not yet implemented")
}

@Suppress("FunctionName")
public fun WebAssemblyMemoryAccess(memory: WebAssemblyMemory): MemoryAccess = WebAssemblyMemoryAccessImpl(memory)

private class WebAssemblyMemoryAccessImpl(private val memory: WebAssemblyMemory) : InternalMemoryAccess {
    override fun createArena(): MemoryArena = WebAssemblyMemoryArena(memory)
}

private class WebAssemblyMemoryArena(private val memory: WebAssemblyMemory) : InternalMemoryArena {
    private val blocks = ArrayDeque<WebAssemblyMemoryBlock>()
    override fun createArena(): MemoryArena {
        // TODO: parent-child relationship
        return WebAssemblyMemoryArena(memory)
    }

    override fun close() {
        blocks.forEach(WebAssemblyMemoryBlock::release)
        blocks.clear()
    }

    override fun UnsafeMemoryAccess.allocate(size: MemorySizeInt, alignment: MemorySizeInt): MemoryBlock {
        return WebAssemblyMemoryBlock(memory, memory.alloc(size), size).also {
            blocks += it
        }
    }

    override fun UnsafeMemoryAccess.allocate(layout: MemoryLayout): MemoryBlock {
        TODO("Not yet implemented")
    }

    override fun UnsafeMemoryAccess.allocateArray(elementLayout: MemoryLayout, elementsCount: Int): MemoryBlock {
        TODO("Not yet implemented")
    }

    override fun UnsafeMemoryAccess.allocateString(value: String): MemoryBlock {
        TODO("Not yet implemented")
    }
}

//@ForeignMemoryApi
//internal sealed class WasmMemoryArena(protected val memory: WasmMemory) : MemoryArena() {
//    override fun wrap(address: MemoryAddressSize, layout: MemoryBlockLayout): MemoryBlock? {
//        return WasmMemoryBlock.fromAddress(address, layout, memory)
//    }
//
//    class Shared(memory: WasmMemory) : WasmMemoryArena(memory) {
//        private val actions = mutableListOf<() -> Unit>()
//        private val cleaner = createCleaner(FreeAction(actions))
//
//        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemoryBlock {
//            val address = memory.malloc(size)
//            val segment = WasmMemoryBlock(address, size, memory, null)
//
//            //TODO: will it work correctly?
//            val memory = this.memory
//            actions.add {
//                segment.makeInaccessible()
//                memory.free(segment.address)
//            }
//            return segment
//        }
//
//        override fun close() {
//            cleaner.clean()
//        }
//
//        override fun invokeOnClose(block: () -> Unit) {
//            actions.add(block)
//        }
//
//        private class FreeAction(private val actions: MutableList<() -> Unit>) : CleanupAction() {
//            override fun run() {
//                actions.forEach { it.runCatching { invoke() } }
//                actions.clear()
//            }
//        }
//    }
//
//    class Implicit(memory: WasmMemory) : WasmMemoryArena(memory) {
//        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemoryBlock {
//            val address = memory.malloc(size)
//            val cleaner = createCleaner(FreeAction(memory, address))
//            return WasmMemoryBlock(address, size, memory, cleaner)
//        }
//
//        override fun invokeOnClose(block: () -> Unit) {
//            TODO("should not be called on implicit scope")
//        }
//
//        override fun close() {
//            TODO("should not be called on implicit scope")
//        }
//
//        private class FreeAction(private val memory: WasmMemory, private val address: MemoryAddressSize) :
//            CleanupAction() {
//            override fun run(): Unit = memory.free(address)
//        }
//    }
//}
