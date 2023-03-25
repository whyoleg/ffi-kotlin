package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

//TODO: COpaque, CUnion
@OptIn(ForeignMemoryApi::class)
@SubclassOptInRequired(ForeignMemoryApi::class)
public abstract class CStruct
@ForeignMemoryApi
constructor(
    segment: MemorySegment,
) : MemoryHolder(segment) {
//    public abstract class Accessor<KT : CStruct>(
//        offset: MemoryAddressSize,
//    ) : MemoryAccessor.Segment<KT>(offset) {
//        abstract override val layout: Layout<KT>
//    }

    public abstract class Type<KT : CStruct>(

    ) {
        private var _size: MemoryAddressSize = 0
        private fun offset(layout: MemoryLayout<*>): MemoryAddressSize {
            val offset = _size
            _size += layout.size
            return offset
        }

        protected fun byte(): MemoryLayout.Byte = MemoryLayout.Byte(offset(MemoryLayout.Byte(0)))
        protected fun int(): MemoryLayout.Byte = TODO()
        protected fun uInt(): MemoryLayout.Byte = TODO()
        protected fun pointer(): CPointer.Layout<Any> = CPointer.Layout(offset(MemoryLayout.Address))
        protected fun <KT : Any> pointer(layout: MemoryLayout<KT>): CPointer.Layout<KT> = CPointer.Layout(offset(layout))
        protected fun <KT : CStruct> struct(layout: Layout<KT>): Layout<KT> = TODO()
    }

    public abstract class Layout<KT : CStruct>(
        offset: MemoryAddressSize,
        public val type: Type<KT>,
    ) : MemoryLayout.Segment<KT>(offset) {
        final override val alignment: MemoryAddressSize
            get() = TODO("Not yet implemented")
        final override val size: MemoryAddressSize get() = _size
    }
}

public var <KT : CStruct> CPointer<KT>.value: KT by KT.Accessor(0)

public inline operator fun <KT : CStruct> CPointer<KT>.getValue(thisRef: Any?, property: KProperty<*>): KT = value
public inline operator fun <KT : CStruct> CPointer<KT>.setValue(thisRef: Any?, property: KProperty<*>, value: KT): Unit {
    this.value = value
}

public fun <KT : CStruct> MemoryScope.pointer(layout: KT.Layout, block: KT.() -> Unit): CPointer<KT> {
    return pointerFor(layout).apply { value.block() }
}

public fun <KT : CStruct> MemoryScope.struct(layout: KT.Layout, block: KT.() -> Unit = {}): KT {
    return pointerFor(layout).value.apply(block)
}

public fun <KT : CStruct> MemoryScope.pointerFor(layout: KT.Layout, value: KT): CPointer<KT> {
    return pointerFor(layout).apply { this.value = value }
}
