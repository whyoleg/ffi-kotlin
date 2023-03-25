package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

//TODO: COpaque, CUnion
@SubclassOptInRequired(ForeignMemoryApi::class)
public abstract class CStruct
@ForeignMemoryApi
constructor(
    public val segment: MemorySegment,
) {
    @ForeignMemoryApi
    protected inline operator fun <KT> MemoryAccessor<KT>.getValue(thisRef: Any?, property: KProperty<*>): KT =
        get(segment)

    @ForeignMemoryApi
    protected inline operator fun <KT> MemoryAccessor<KT>.setValue(thisRef: Any?, property: KProperty<*>, value: KT): Unit =
        set(segment, value)
}

public fun <KT : CStruct> MemoryScope.pointer(type: CType<KT>, block: KT.() -> Unit): CPointer<KT> {
    return pointerFor(type).apply { value.block() }
}

public fun <KT : CStruct> MemoryScope.struct(type: CType<KT>, block: KT.() -> Unit = {}): KT {
    return pointerFor(type).value.apply(block)
}
