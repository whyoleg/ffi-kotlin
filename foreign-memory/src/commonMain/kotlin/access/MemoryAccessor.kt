package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*
import kotlin.reflect.*

@ForeignMemoryApi
public sealed class MemoryAccessor<KT : Any>(public val offset: MemoryAddressSize) {
    public abstract val layout: MemoryLayout

    //TODO: rename get/set and make internal
    public abstract fun get(segment: MemorySegment): KT?
    public abstract fun set(segment: MemorySegment, value: KT?)

    public abstract fun at(offset: MemoryAddressSize): MemoryAccessor<KT>

    public companion object {
        public val Void: MemoryAccessor<Unit> get() = VoidMemoryAccessor
        public val Byte: MemoryAccessor<Byte> = ByteMemoryAccessor(MemoryAddressSize.ZERO)
        public val Int: MemoryAccessor<Int> = IntMemoryAccessor(MemoryAddressSize.ZERO)
        public val UInt: MemoryAccessor<UInt> = UIntMemoryAccessor(MemoryAddressSize.ZERO)
        public val PlatformInt: MemoryAccessor<PlatformInt> = PlatformIntMemoryAccessor(MemoryAddressSize.ZERO)
        public val PlatformUInt: MemoryAccessor<PlatformUInt> = PlatformUIntMemoryAccessor(MemoryAddressSize.ZERO)
    }
}

@ForeignMemoryApi
public inline operator fun <KT : Any> MemoryAccessor<KT>.getValue(thisRef: MemoryHolder, property: KProperty<*>): KT? =
    get(thisRef.segment)

@ForeignMemoryApi
public inline operator fun <KT : Any> MemoryAccessor<KT>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: KT?): Unit =
    set(thisRef.segment, value)
