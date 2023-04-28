package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class PlatformIntMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<PlatformInt>(offset) {
    override val layout: MemoryBlockLayout get() = MemoryBlockLayout.PlatformInt
    override fun get(block: MemoryBlock): PlatformInt = getRaw(block)
    override fun set(block: MemoryBlock, value: PlatformInt?): Unit = setRaw(block, value ?: 0.toPlatformInt())
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<PlatformInt> = PlatformIntMemoryAccessor(offset)
}

@ForeignMemoryApi
public inline fun MemoryAccessor<out PlatformInt>.getRaw(block: MemoryBlock): PlatformInt =
    block.loadPlatformInt(offset)

@ForeignMemoryApi
public inline fun MemoryAccessor<out PlatformInt>.setRaw(block: MemoryBlock, value: PlatformInt): Unit =
    block.storePlatformInt(offset, value)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<out PlatformInt>.getValue(
    thisRef: MemoryHolder,
    property: KProperty<*>
): PlatformInt = getRaw(thisRef.blockInternal)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<out PlatformInt>.setValue(
    thisRef: MemoryHolder,
    property: KProperty<*>,
    value: PlatformInt
): Unit = setRaw(thisRef.blockInternal, value)
