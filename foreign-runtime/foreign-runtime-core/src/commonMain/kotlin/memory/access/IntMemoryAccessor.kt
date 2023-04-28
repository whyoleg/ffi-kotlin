package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class IntMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<Int>(offset) {
    override val layout: MemoryBlockLayout get() = MemoryBlockLayout.Int
    override fun get(block: MemoryBlock): Int = getRaw(block)
    override fun set(block: MemoryBlock, value: Int?): Unit = setRaw(block, value ?: 0)
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<Int> = IntMemoryAccessor(offset)
}

@ForeignMemoryApi
public inline fun MemoryAccessor<Int>.getRaw(block: MemoryBlock): Int = block.loadInt(offset)

@ForeignMemoryApi
public inline fun MemoryAccessor<Int>.setRaw(block: MemoryBlock, value: Int): Unit = block.storeInt(offset, value)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<Int>.getValue(thisRef: MemoryHolder, property: KProperty<*>): Int =
    getRaw(thisRef.blockInternal)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<Int>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: Int): Unit =
    setRaw(thisRef.blockInternal, value)
