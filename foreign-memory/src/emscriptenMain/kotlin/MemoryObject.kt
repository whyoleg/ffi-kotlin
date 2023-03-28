package dev.whyoleg.foreign.memory

public actual abstract class MemoryObject {
    @ForeignMemoryApi
    protected abstract val memory: WasmMemory
    public actual val autoScope: MemoryScope get() = TODO()
    public actual fun createScope(): MemoryScope.Closeable = TODO()

    public actual object Default : MemoryObject() {
        @ForeignMemoryApi
        override val memory: WasmMemory get() = TODO()
    }

    public actual companion object
}
