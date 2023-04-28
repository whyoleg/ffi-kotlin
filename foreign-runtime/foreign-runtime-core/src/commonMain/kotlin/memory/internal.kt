package dev.whyoleg.foreign.memory

@ForeignMemoryApi
internal expect fun createNullMemoryBlock(): MemoryBlock

@ForeignMemoryApi
internal expect fun createVoidMemoryBlockLayout(): MemoryBlockLayout

@ForeignMemoryApi
internal expect fun createByteMemoryBlockLayout(): MemoryBlockLayout

@ForeignMemoryApi
internal expect fun createIntMemoryBlockLayout(): MemoryBlockLayout

@ForeignMemoryApi
internal expect fun createLongMemoryBlockLayout(): MemoryBlockLayout

@ForeignMemoryApi
internal expect fun createPlatformIntMemoryBlockLayout(): MemoryBlockLayout

@ForeignMemoryApi
internal expect fun createAddressMemoryBlockLayout(): MemoryBlockLayout

@ForeignMemoryApi
internal expect fun createRecordMemoryBlockLayout(isUnion: Boolean): MemoryBlockLayout.Record

@ForeignMemoryApi
internal expect fun createAutoMemoryAccess(): MemoryAccess
