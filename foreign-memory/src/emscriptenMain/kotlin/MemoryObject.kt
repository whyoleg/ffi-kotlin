package dev.whyoleg.foreign.memory

public actual abstract class MemoryObject {
    public actual val autoScope: MemoryScope get() = TODO()
    public actual fun createScope(): MemoryScope.Closeable = TODO()

    public actual object Default : MemoryObject()
}
