package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class PlatformUIntMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<PlatformUInt>(offset) {
    override val layout: MemoryBlockLayout get() = MemoryBlockLayout.PlatformInt
    override fun get(block: MemoryBlock): PlatformUInt = getRaw(block)
    override fun set(block: MemoryBlock, value: PlatformUInt?): Unit = setRaw(block, value ?: 0.toPlatformUInt())
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<PlatformUInt> = PlatformUIntMemoryAccessor(offset)
}

@ForeignMemoryApi
public inline fun MemoryAccessor<out PlatformUInt>.getRaw(block: MemoryBlock): PlatformUInt =
    block.loadPlatformInt(offset).toPlatformUInt()

@ForeignMemoryApi
public inline fun MemoryAccessor<out PlatformUInt>.setRaw(block: MemoryBlock, value: PlatformUInt): Unit =
    block.storePlatformInt(offset, value.toPlatformInt())

@ForeignMemoryApi
public inline operator fun MemoryAccessor<out PlatformUInt>.getValue(
    thisRef: MemoryHolder,
    property: KProperty<*>
): PlatformUInt =
    getRaw(thisRef.blockInternal)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<out PlatformUInt>.setValue(
    thisRef: MemoryHolder,
    property: KProperty<*>,
    value: PlatformUInt
): Unit =
    setRaw(thisRef.blockInternal, value)
