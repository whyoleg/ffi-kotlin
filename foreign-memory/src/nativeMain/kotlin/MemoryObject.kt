package dev.whyoleg.foreign.memory

public actual abstract class MemoryObject private constructor() {
    public actual val autoScope: MemoryScope get() = MemoryScope.Auto
    public actual fun createScope(): MemoryScope.Closeable = MemoryScope.Impl()

    public actual object Default : MemoryObject()

    public actual companion object
}
