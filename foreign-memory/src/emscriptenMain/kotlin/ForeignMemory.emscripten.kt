package dev.whyoleg.foreign.memory

@OptIn(ForeignMemoryApi::class)
public actual class ForeignMemory
@ForeignMemoryApi
constructor(
    private val memory: WasmMemory
) {
    public actual companion object {
        public actual val Default: ForeignMemory = ForeignMemory(WasmMemory.default())
    }

    public actual val implicit: MemoryArena = MemoryArenaImpl.Implicit(memory)
    public actual fun createArena(): MemoryArena = MemoryArenaImpl.Shared(memory)
}
