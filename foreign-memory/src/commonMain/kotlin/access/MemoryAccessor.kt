package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*
import kotlin.reflect.*

//KT - Kotlin representation, could be:
// * Primitive(Int, Long, etc)
// * Unit (similar to opaque)
// * EmptyMemoryValue (Opaque)
// * MemoryValue (Struct / Union)
// * MemoryReference (CPointer)
@ForeignMemoryApi
public sealed class MemoryAccessor<KT : Any>(public val offset: MemoryAddressSize) {
    public abstract val layout: MemoryLayout

    //TODO: rename get/set and make internal
    public abstract fun get(segment: MemorySegment): KT?
    public abstract fun set(segment: MemorySegment, value: KT?)

    protected abstract fun withOffset(offset: MemoryAddressSize): MemoryAccessor<KT>

    public fun at(offset: MemoryAddressSize): MemoryAccessor<KT> {
        if (this.offset == offset) return this
        return withOffset(offset)
    }

    public companion object {
        public val Void: MemoryAccessor<Unit> get() = VoidMemoryAccessor
        public val Byte: MemoryAccessor<Byte> = ByteMemoryAccessor(memoryAddressSizeZero())
        public val Int: MemoryAccessor<Int> = IntMemoryAccessor(memoryAddressSizeZero())
        public val UInt: MemoryAccessor<UInt> = UIntMemoryAccessor(memoryAddressSizeZero())
        public val PlatformInt: MemoryAccessor<PlatformInt> = PlatformIntMemoryAccessor(memoryAddressSizeZero())
        public val PlatformUInt: MemoryAccessor<PlatformUInt> = PlatformUIntMemoryAccessor(memoryAddressSizeZero())
    }
}

@ForeignMemoryApi
public inline operator fun <KT : Any> MemoryAccessor<KT>.getValue(thisRef: MemoryHolder, property: KProperty<*>): KT? =
    get(thisRef.segment)

@ForeignMemoryApi
public inline operator fun <KT : Any> MemoryAccessor<KT>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: KT?): Unit =
    set(thisRef.segment, value)
