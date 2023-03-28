package dev.whyoleg.foreign.memory

public actual class MemoryObject private constructor() {
    public actual val autoScope: MemoryScope get() = MemoryScope.Auto
    public actual fun createScope(): MemoryScope.Closeable = MemoryScope.Closeable()

    public actual companion object {
        public actual val Default: MemoryObject = MemoryObject()
    }
}
