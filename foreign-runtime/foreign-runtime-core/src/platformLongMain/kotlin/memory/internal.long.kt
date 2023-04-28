package dev.whyoleg.foreign.memory

@ForeignMemoryApi
internal actual fun createPlatformIntMemoryBlockLayout(): MemoryBlockLayout = createLongMemoryBlockLayout()
