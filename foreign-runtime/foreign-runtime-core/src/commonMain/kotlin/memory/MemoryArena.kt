package dev.whyoleg.foreign.memory

@ForeignMemoryApi
public abstract class MemoryArena internal constructor() : AutoCloseable {
    public abstract fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemoryBlock

    public open fun allocate(layout: MemoryBlockLayout): MemoryBlock {
        return allocate(layout.size, layout.alignment)
    }

    public open fun allocateArray(elementLayout: MemoryBlockLayout, elementsCount: Int): MemoryBlock {
        return allocate(elementLayout.size * elementsCount, elementLayout.alignment)
    }

    public open fun allocateString(value: String): MemoryBlock {
        val bytes = value.encodeToByteArray()
        return allocate(memoryAddressSize(bytes.size + 1), memoryAddressSize(1)).also {
            it.storeByteArray(memoryAddressSizeZero(), bytes)
            it.storeByte(memoryAddressSize(bytes.size), 0)
        }
    }

    public abstract fun wrap(address: MemoryAddressSize, layout: MemoryBlockLayout): MemoryBlock?

    //TODO: name, contract?
    public abstract fun invokeOnClose(block: () -> Unit)
}
