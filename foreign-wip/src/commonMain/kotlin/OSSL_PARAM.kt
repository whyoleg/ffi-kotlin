package dev.whyoleg.foreign.wip

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*
import kotlin.reflect.*

@OptIn(ForeignMemoryApi::class)
public var CPointer<OSSL_PARAM>.value: OSSL_PARAM by OSSL_PARAM.Layout(0)

public inline operator fun CPointer<OSSL_PARAM>.getValue(thisRef: Any?, property: KProperty<*>): OSSL_PARAM = value
public inline operator fun CPointer<OSSL_PARAM>.setValue(thisRef: Any?, property: KProperty<*>, value: OSSL_PARAM): Unit {
    this.value = value
}

public fun MemoryScope.pointer(layout: OSSL_PARAM.Layout, block: OSSL_PARAM.() -> Unit): CPointer<OSSL_PARAM> {
    return pointerFor(layout).apply { value.block() }
}

public fun MemoryScope.struct(layout: OSSL_PARAM.Layout, block: OSSL_PARAM.() -> Unit = {}): OSSL_PARAM {
    return pointerFor(layout).value.apply(block)
}

public fun MemoryScope.pointerFor(layout: OSSL_PARAM.Layout, value: OSSL_PARAM): CPointer<OSSL_PARAM> {
    return pointerFor(layout).apply { this.value = value }
}

@OptIn(ForeignMemoryApi::class)
@Suppress("ClassName", "PropertyName")
public class OSSL_PARAM private constructor(segment: MemorySegment) : CStruct(segment) {
    public var key: CString? by Type.key

    //    public var data_type: UInt by Layout.data_type
    public var data: CPointer<Any>? by Type.data
//    public var data_size: PlatformUInt by Layout.data_size
//    public var return_size: PlatformUInt by Layout.return_size

    public companion object Layout : MemoryLayout<OSSL_PARAM> {
        private val key = pointer(MemoryLayout.Byte(0))
        private val data = pointer()
        private val nested = struct(OSSL_PARAM.Layout(0))
    }

    public class Handle(offset: MemoryAddressSize) : CStruct.Layout<OSSL_PARAM>(offset, Type) {
        override fun wrap(segment: MemorySegment): OSSL_PARAM = OSSL_PARAM(segment)
    }
}
