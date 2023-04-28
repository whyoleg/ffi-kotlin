package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class ULongMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<ULong>(offset) {
    override val layout: MemoryBlockLayout get() = MemoryBlockLayout.Long
    override fun get(block: MemoryBlock): ULong = getRaw(block)
    override fun set(block: MemoryBlock, value: ULong?): Unit = setRaw(block, value ?: 0U)
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<ULong> = ULongMemoryAccessor(offset)
}

@ForeignMemoryApi
public inline fun MemoryAccessor<ULong>.getRaw(block: MemoryBlock): ULong = block.loadLong(offset).toULong()

@ForeignMemoryApi
public inline fun MemoryAccessor<ULong>.setRaw(block: MemoryBlock, value: ULong): Unit = block.storeLong(offset, value.toLong())

@ForeignMemoryApi
public inline operator fun MemoryAccessor<ULong>.getValue(thisRef: MemoryHolder, property: KProperty<*>): ULong =
    getRaw(thisRef.blockInternal)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<ULong>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: ULong): Unit =
    setRaw(thisRef.blockInternal, value)
