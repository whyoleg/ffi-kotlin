package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
public sealed class MemoryAccessor<KT : Any>(public val offset: MemoryAddressSize) {
    public abstract val layout: MemoryLayout
    public abstract fun get(segment: MemorySegment): KT?
    public abstract fun set(segment: MemorySegment, value: KT?)
    public abstract fun at(offset: MemoryAddressSize): MemoryAccessor<KT>

    public object Void : MemoryAccessor<Unit>(0) {
        override val layout: MemoryLayout get() = MemoryLayout.Void
        override fun get(segment: MemorySegment): Unit = Unit
        override fun set(segment: MemorySegment, value: Unit?): Unit = Unit
        override fun at(offset: MemoryAddressSize): MemoryAccessor<Unit> = this
    }

    public open class Byte private constructor(offset: MemoryAddressSize) : MemoryAccessor<kotlin.Byte>(offset) {
        override val layout: MemoryLayout get() = MemoryLayout.Byte
        override fun get(segment: MemorySegment): kotlin.Byte = getRaw(segment)
        override fun set(segment: MemorySegment, value: kotlin.Byte?): Unit = setRaw(segment, value ?: 0)
        override fun at(offset: MemoryAddressSize): Byte = Byte(offset)

        public companion object : Byte(0)
    }

    public open class Int private constructor(offset: MemoryAddressSize) : MemoryAccessor<kotlin.Int>(offset) {
        override val layout: MemoryLayout get() = MemoryLayout.Int
        override fun get(segment: MemorySegment): kotlin.Int = getRaw(segment)
        override fun set(segment: MemorySegment, value: kotlin.Int?): Unit = setRaw(segment, value ?: 0)
        override fun at(offset: MemoryAddressSize): Int = Int(offset)

        public companion object : Int(0)
    }

    public open class UInt private constructor(offset: MemoryAddressSize) : MemoryAccessor<kotlin.UInt>(offset) {
        override val layout: MemoryLayout get() = MemoryLayout.Int
        override fun get(segment: MemorySegment): kotlin.UInt = getRaw(segment)
        override fun set(segment: MemorySegment, value: kotlin.UInt?): Unit = setRaw(segment, value ?: 0U)
        override fun at(offset: MemoryAddressSize): UInt = UInt(offset)

        public companion object : UInt(0)
    }

    public abstract class Address<Ref : MemoryHolder, PKT : Any>(offset: MemoryAddressSize) : MemoryAccessor<Ref>(offset) {
        override val layout: MemoryLayout get() = MemoryLayout.Address
        protected abstract val pointedAccessor: MemoryAccessor<PKT>
        protected abstract fun wrap(segment: MemorySegment): Ref

        override fun get(segment: MemorySegment): Ref? = segment.loadAddress(offset, pointedAccessor.layout)?.let(::wrap)
        override fun set(segment: MemorySegment, value: Ref?): Unit = segment.storeAddress(offset, pointedAccessor.layout, value?.segment)
    }

    public abstract class Segment<KT : MemoryHolder>(offset: MemoryAddressSize) : MemoryAccessor<KT>(offset) {
        protected abstract fun wrap(segment: MemorySegment): KT

        override fun get(segment: MemorySegment): KT = wrap(segment.loadSegment(offset, layout))
        override fun set(segment: MemorySegment, value: KT?): Unit =
            segment.storeSegment(offset, layout, value?.segment ?: MemorySegment.Empty)
    }
}

@ForeignMemoryApi
public inline operator fun <KT : Any> MemoryAccessor<KT>.getValue(thisRef: MemoryHolder, property: KProperty<*>): KT? =
    get(thisRef.segment)

@ForeignMemoryApi
public inline operator fun <KT : Any> MemoryAccessor<KT>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: KT?): Unit =
    set(thisRef.segment, value)
