package dev.whyoleg.foreign.memory

@OptIn(ForeignMemoryApi::class)
internal object NativeMemoryAccess : MemoryAccess() {
    override val implicit: MemoryArena get() = NativeMemoryArena.Implicit
    override fun createArena(): MemoryArena = NativeMemoryArena.Shared()
}
