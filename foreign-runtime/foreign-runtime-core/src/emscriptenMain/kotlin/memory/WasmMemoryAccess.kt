package dev.whyoleg.foreign.memory

@ForeignMemoryApi
internal class WasmMemoryAccess(
    private val memory: WasmMemory
) : MemoryAccess() {
    override val implicit: MemoryArena = WasmMemoryArena.Implicit(memory)
    override fun createArena(): MemoryArena = WasmMemoryArena.Shared(memory)
}
