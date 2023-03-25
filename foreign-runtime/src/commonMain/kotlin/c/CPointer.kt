package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import kotlin.properties.*
import kotlin.reflect.*

@OptIn(ForeignMemoryApi::class)
public class CPointer<KT>
@ForeignMemoryApi
internal constructor(
    public val segment: MemorySegment,
    private val accessor: MemoryAccessor<KT>,
) : ReadWriteProperty<Any?, KT> {
    public var value: KT
        get() = accessor.get(segment)
        set(value) = accessor.set(segment, value)

    override fun getValue(thisRef: Any?, property: KProperty<*>): KT = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: KT): Unit = run { this.value = value }

    @ForeignMemoryApi
    internal class Accessor<KT>(
        private val pointedLayout: MemoryLayout,
        private val pointedAccessor: MemoryAccessor<KT>,
        override val offset: MemoryAddressSize,
    ) : MemoryAccessor<CPointer<KT>?>() {

        override fun get(segment: MemorySegment): CPointer<KT>? {
            return segment.loadAddress(offset, pointedLayout)?.let { CPointer(it, pointedAccessor) }
        }

        override fun set(segment: MemorySegment, value: CPointer<KT>?) {
            segment.storeAddress(offset, pointedLayout, value?.segment)
        }
    }
}

@OptIn(ForeignMemoryApi::class)
public fun <KT> MemoryScope.pointerFor(type: CType<KT>): CPointer<KT> {
    return CPointer(allocateMemory(type.layout), type.accessor(0))
}

@OptIn(ForeignMemoryApi::class)
public fun <KT> MemoryScope.pointerFor(type: CType<KT>, value: KT): CPointer<KT> {
    return CPointer(allocateMemory(type.layout), type.accessor(0)).apply { this.value = value }
}

public fun <KT> MemoryScope.pointerTo(type: CType<KT>, value: CPointer<KT>?): CPointer<CPointer<KT>?> {
    return pointerFor(CType.Pointer(type)).apply { this.value = value }
}
