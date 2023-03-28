package dev.whyoleg.foreign.memory

@ForeignMemoryApi
public abstract class WasmMemory {
    public abstract fun malloc(size: MemoryAddressSize): MemoryAddressSize
    public abstract fun free(address: MemoryAddressSize)
    public abstract fun loadByte(address: MemoryAddressSize): Byte
    public abstract fun storeByte(address: MemoryAddressSize, value: Byte)
    public abstract fun loadInt(address: MemoryAddressSize): Int
    public abstract fun storeInt(address: MemoryAddressSize, value: Int)
}
