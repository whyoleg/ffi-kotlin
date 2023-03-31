package dev.whyoleg.foreign.memory

// TODO: decide on naming
public expect class MemoryObject {
    public val autoAllocator: MemoryAllocator
    public fun createScope(): MemoryScope

    //TODO: decide on naming
    @ForeignMemoryApi
    public fun unsafeMemory(address: MemoryAddress, layout: MemoryLayout): MemorySegment?

    public companion object {
        public val Default: MemoryObject
    }
}

//TODO: naming...
public fun interface MemoryAllocator {
    @ForeignMemoryApi
    public fun allocateMemory(size: MemoryAddressSize, alignment: MemoryAddressSize): MemorySegment

    @ForeignMemoryApi
    public fun allocateMemory(layout: MemoryLayout): MemorySegment = allocateMemory(layout.size, layout.alignment)

    @ForeignMemoryApi
    public fun allocateMemoryArray(elementLayout: MemoryLayout, elementsCount: Int): MemorySegment =
        allocateMemory(elementLayout.size * elementsCount, elementLayout.alignment)

    //TODO?
    @ForeignMemoryApi
    public fun allocateMemoryString(value: String): MemorySegment {
        val bytes = value.encodeToByteArray()
        return allocateMemory(memoryAddressSize(bytes.size), memoryAddressSize(1)).apply {
            storeByteArray(memoryAddressSizeZero(), bytes)
            storeByte(memoryAddressSize(bytes.size), 0)
        }
    }
}

//TODO: add AutoCloseable later, because now there is an error on jvm when using it...
public interface MemoryScope : MemoryAllocator {
    public fun close()
}

public inline fun <T> memoryScoped(obj: MemoryObject = MemoryObject.Default, block: MemoryScope.() -> T): T {
    //return obj.createScope().use(block)
    val scope = obj.createScope()
    try {
        return scope.block()
    } finally {
        scope.close()
    }
}
