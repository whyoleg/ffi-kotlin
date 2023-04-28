package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class LongMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<Long>(offset) {
    override val layout: MemoryBlockLayout get() = MemoryBlockLayout.Long
    override fun get(block: MemoryBlock): Long = getRaw(block)
    override fun set(block: MemoryBlock, value: Long?): Unit = setRaw(block, value ?: 0)
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<Long> = LongMemoryAccessor(offset)
}

@ForeignMemoryApi
public inline fun MemoryAccessor<Long>.getRaw(block: MemoryBlock): Long = block.loadLong(offset)

@ForeignMemoryApi
public inline fun MemoryAccessor<Long>.setRaw(block: MemoryBlock, value: Long): Unit = block.storeLong(offset, value)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<Long>.getValue(thisRef: MemoryHolder, property: KProperty<*>): Long =
    getRaw(thisRef.blockInternal)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<Long>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: Long): Unit =
    setRaw(thisRef.blockInternal, value)
