package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.nio.*
import dev.whyoleg.foreign.shared.*

@OptIn(ForeignMemoryApi::class)
internal actual fun createAutoMemoryAccess(): MemoryAccess = ByteBufferMemoryAccess

@ForeignMemoryApi
internal actual fun createNullMemoryBlock(): MemoryBlock = ByteBufferMemoryBlock.NULL

@ForeignMemoryApi
internal actual fun createVoidMemoryBlockLayout(): MemoryBlockLayout = SharedMemoryBlockLayout.void()

@ForeignMemoryApi
internal actual fun createByteMemoryBlockLayout(): MemoryBlockLayout = SharedMemoryBlockLayout.byte()

@ForeignMemoryApi
internal actual fun createIntMemoryBlockLayout(): MemoryBlockLayout = SharedMemoryBlockLayout.int()

@ForeignMemoryApi
internal actual fun createLongMemoryBlockLayout(): MemoryBlockLayout = SharedMemoryBlockLayout.long()

@ForeignMemoryApi
internal actual fun createAddressMemoryBlockLayout(): MemoryBlockLayout = SharedMemoryBlockLayout.address()

@ForeignMemoryApi
internal actual fun createRecordMemoryBlockLayout(isUnion: Boolean): MemoryBlockLayout.Record = SharedMemoryBlockLayoutRecord(isUnion)
