package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*

//TODO: COpaque, CUnion
@OptIn(ForeignMemoryApi::class)
@SubclassOptInRequired(ForeignMemoryApi::class)
public abstract class CStruct<Self : CStruct<Self>>
@ForeignMemoryApi
constructor(
    segment: MemorySegment,
) : MemoryHolder(segment) {
    public abstract val type: CType.Struct<Self>
}

public fun <KT : CStruct<KT>> MemoryScope.pointer(value: KT): CPointer<KT> = pointerFor(value.type, value)

public fun <KT : CStruct<KT>> MemoryScope.pointer(type: CType.Struct<KT>, block: KT.() -> Unit): CPointer<KT> =
    pointerFor(type).apply { value!!.block() }

// TODO: is it really needed?
@OptIn(ForeignMemoryApi::class)
public fun <KT : CStruct<KT>> MemoryScope.struct(type: CType.Struct<KT>, block: KT.() -> Unit = {}): KT {
    return type.accessor.get(allocateMemory(type.layout))!!.apply(block)
//    return pointerFor(type).value!!.apply(block)
}
