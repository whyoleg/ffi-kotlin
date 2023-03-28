package dev.whyoleg.foreign.memory

public actual class MemoryObject {
    public actual val autoScope: MemoryScope get() = TODO()
    public actual fun createScope(): MemoryScope.Closeable = TODO()

    public actual companion object {
        public actual val Default: MemoryObject get() = TODO()
    }
}
