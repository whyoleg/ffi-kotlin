package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*


@ForeignMemoryApi
public fun <KT : Any> CPointer.Companion.ofAddress(type: CType<KT>, address: MemoryAddress): CPointer<KT>? {

}

//panama
@ForeignMemoryApi
public fun <KT : CStruct<KT>> CStruct.Companion.wrap(type: CType.Struct<KT>, address: MemoryAddress): KT {

}
