package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class ByteMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<Byte>(offset) {
    override val layout: MemoryBlockLayout get() = MemoryBlockLayout.Byte
    override fun get(block: MemoryBlock): Byte = getRaw(block)
    override fun set(block: MemoryBlock, value: Byte?): Unit = setRaw(block, value ?: 0)
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<Byte> = ByteMemoryAccessor(offset)
}

// all those re-declarations are needed to overcome boxing...
@ForeignMemoryApi
public inline fun MemoryAccessor<Byte>.getRaw(block: MemoryBlock): Byte = block.loadByte(offset)

@ForeignMemoryApi
public inline fun MemoryAccessor<Byte>.setRaw(block: MemoryBlock, value: Byte): Unit = block.storeByte(offset, value)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<Byte>.getValue(thisRef: MemoryHolder, property: KProperty<*>): Byte =
    getRaw(thisRef.blockInternal)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<Byte>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: Byte): Unit =
    setRaw(thisRef.blockInternal, value)
