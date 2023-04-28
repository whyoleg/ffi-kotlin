package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class UIntMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<UInt>(offset) {
    override val layout: MemoryBlockLayout get() = MemoryBlockLayout.Int
    override fun get(block: MemoryBlock): UInt = getRaw(block)
    override fun set(block: MemoryBlock, value: UInt?): Unit = setRaw(block, value ?: 0U)
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<UInt> = UIntMemoryAccessor(offset)
}

@ForeignMemoryApi
public inline fun MemoryAccessor<UInt>.getRaw(block: MemoryBlock): UInt = block.loadInt(offset).toUInt()

@ForeignMemoryApi
public inline fun MemoryAccessor<UInt>.setRaw(block: MemoryBlock, value: UInt): Unit = block.storeInt(offset, value.toInt())

@ForeignMemoryApi
public inline operator fun MemoryAccessor<UInt>.getValue(thisRef: MemoryHolder, property: KProperty<*>): UInt =
    getRaw(thisRef.blockInternal)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<UInt>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: UInt): Unit =
    setRaw(thisRef.blockInternal, value)
