package dev.whyoleg.foreign.memory

@OptIn(ForeignMemoryApi::class)
public actual class MemoryObject
@ForeignMemoryApi
constructor(
    private val memory: WasmMemory
) {
    public actual val autoScope: MemoryScope = MemoryScope.Auto(memory)
    public actual fun createScope(): MemoryScope.Closeable = MemoryScope.Closeable(memory)

    public actual companion object {
        public actual val Default: MemoryObject = MemoryObject(WasmMemory.default())
    }
}
