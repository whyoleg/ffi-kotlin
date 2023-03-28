package dev.whyoleg.foreign.memory

@ForeignMemoryApi
public abstract class WasmMemory {
    public abstract fun malloc(size: MemoryAddressSize): MemoryAddress
    public abstract fun free(address: MemoryAddress)
    public abstract fun loadByte(address: MemoryAddressSize): Byte
    public abstract fun storeByte(address: MemoryAddressSize, value: Byte)
    public abstract fun loadInt(address: MemoryAddressSize): Int
    public abstract fun storeInt(address: MemoryAddressSize, value: Int)

    internal object Empty : WasmMemory() {
        override fun malloc(size: MemoryAddressSize): MemoryAddress = TODO("should not be called")
        override fun free(address: MemoryAddress): Unit = TODO("should not be called")
        override fun loadByte(address: MemoryAddressSize): Byte = TODO("should not be called")
        override fun storeByte(address: MemoryAddressSize, value: Byte) = TODO("should not be called")
        override fun loadInt(address: MemoryAddressSize): Int = TODO("should not be called")
        override fun storeInt(address: MemoryAddressSize, value: Int) = TODO("should not be called")
    }

    public companion object {
        private val memories = mutableSetOf<WasmMemory>()

        public fun register(memory: WasmMemory) {
            memories.add(memory)
        }

        internal fun default(): WasmMemory {
            check(memories.size == 1) {
                "Multiple registered memories: ${memories}. Automatic selection of WasmMemory is not supported"
            }
            return memories.single()
        }
    }
}
