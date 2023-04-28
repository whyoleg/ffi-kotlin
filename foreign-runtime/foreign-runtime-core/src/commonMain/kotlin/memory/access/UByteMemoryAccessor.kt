package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class UByteMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<UByte>(offset) {
    override val layout: MemoryBlockLayout get() = MemoryBlockLayout.Byte
    override fun get(block: MemoryBlock): UByte = getRaw(block)
    override fun set(block: MemoryBlock, value: UByte?): Unit = setRaw(block, value ?: 0U)
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<UByte> = UByteMemoryAccessor(offset)
}

// all those re-declarations are needed to overcome boxing...
@ForeignMemoryApi
public inline fun MemoryAccessor<UByte>.getRaw(block: MemoryBlock): UByte = block.loadByte(offset).toUByte()

@ForeignMemoryApi
public inline fun MemoryAccessor<UByte>.setRaw(block: MemoryBlock, value: UByte): Unit = block.storeByte(offset, value.toByte())

@ForeignMemoryApi
public inline operator fun MemoryAccessor<UByte>.getValue(thisRef: MemoryHolder, property: KProperty<*>): UByte =
    getRaw(thisRef.blockInternal)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<UByte>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: UByte): Unit =
    setRaw(thisRef.blockInternal, value)
