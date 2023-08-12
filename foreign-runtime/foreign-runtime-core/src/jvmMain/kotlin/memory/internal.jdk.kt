package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.ffm.*
import dev.whyoleg.foreign.nio.*
import dev.whyoleg.foreign.shared.*
import java.lang.foreign.*

//TODO: add here also fallback to unsafe
private val foreignAvailable = try {
    //will fail on JVM runtime 8, so we need to do try catch :)
    Runtime.version().feature() == 20
} catch (cause: Throwable) {
    false
}

@OptIn(ForeignMemoryApi::class)
internal actual fun createAutoMemoryAccess(): MemoryAccess = when {
    foreignAvailable -> FfmMemoryAccess
    else             -> ByteBufferMemoryAccess
}

@ForeignMemoryApi
internal actual fun createNullMemoryBlock(): MemoryBlock = when {
    foreignAvailable -> FfmMemoryBlock.NULL
    else             -> ByteBufferMemoryBlock.NULL
}

@ForeignMemoryApi
internal actual fun createVoidMemoryBlockLayout(): MemoryBlockLayout = when {
    foreignAvailable -> FfmMemoryBlockLayout(MemoryLayout.structLayout())
    else             -> SharedMemoryBlockLayout.void()
}

@ForeignMemoryApi
internal actual fun createByteMemoryBlockLayout(): MemoryBlockLayout = when {
    foreignAvailable -> FfmMemoryBlockLayout(ValueLayout.JAVA_BYTE)
    else             -> SharedMemoryBlockLayout.byte()
}

@ForeignMemoryApi
internal actual fun createIntMemoryBlockLayout(): MemoryBlockLayout = when {
    foreignAvailable -> FfmMemoryBlockLayout(ValueLayout.JAVA_INT)
    else             -> SharedMemoryBlockLayout.int()
}

@ForeignMemoryApi
internal actual fun createLongMemoryBlockLayout(): MemoryBlockLayout = when {
    foreignAvailable -> FfmMemoryBlockLayout(ValueLayout.JAVA_LONG)
    else             -> SharedMemoryBlockLayout.long()
}

@ForeignMemoryApi
internal actual fun createAddressMemoryBlockLayout(): MemoryBlockLayout = when {
    foreignAvailable -> FfmMemoryBlockLayout(ValueLayout.ADDRESS)
    else             -> SharedMemoryBlockLayout.address()
}

@ForeignMemoryApi
internal actual fun createRecordMemoryBlockLayout(isUnion: Boolean): MemoryBlockLayout.Record = when {
    foreignAvailable -> FfmMemoryBlockLayoutRecord(isUnion)
    else             -> SharedMemoryBlockLayoutRecord(isUnion)
}
