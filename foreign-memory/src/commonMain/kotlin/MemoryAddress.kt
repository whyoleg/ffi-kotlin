package dev.whyoleg.foreign.memory

@Suppress("NO_ACTUAL_FOR_EXPECT")
@ForeignMemoryApi
public expect class MemoryAddress

//TODO: introduce some kind of `InteropScope`, only in which we can access unsafe things
//TODO: somehow hide those values...
//TODO: create additional package like foreign.invoke/foreign.function/foreign.interop, etc
@ForeignMemoryApi
public val MemoryHolder?.address: MemoryAddress get() = (this?.segment ?: MemorySegment.Empty).address
