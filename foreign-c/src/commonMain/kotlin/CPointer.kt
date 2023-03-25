package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@OptIn(ForeignMemoryApi::class)
public class CPointer<KT : Any>
@PublishedApi
internal constructor(
    segment: MemorySegment,
) : MemoryHolder(segment) {
    public class Layout<KT : Any>(offset: MemoryAddressSize = 0) : MemoryLayout.Address<CPointer<KT>>(offset) {
        override fun wrap(segment: MemorySegment): CPointer<KT> = CPointer(segment)
    }
}

@OptIn(ForeignMemoryApi::class)
public var <KT : Any> CPointer<CPointer<KT>>.value: CPointer<KT>? by CPointer.Layout()

public inline operator fun <KT : Any> CPointer<CPointer<KT>>.getValue(thisRef: Any?, property: KProperty<*>): CPointer<KT>? = value
public inline operator fun <KT : Any> CPointer<CPointer<KT>>.setValue(thisRef: Any?, property: KProperty<*>, value: CPointer<KT>?): Unit {
    this.value = value
}

@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> MemoryScope.pointerFor(value: CPointer<KT>?): CPointer<CPointer<KT>> {
    return CPointer<CPointer<KT>>(allocateMemory(MemoryLayout.Address)).apply { this.value = value }
}

@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> MemoryScope.pointerFor(layout: MemoryLayout<KT>): CPointer<KT> {
    return CPointer(allocateMemory(layout))
}
