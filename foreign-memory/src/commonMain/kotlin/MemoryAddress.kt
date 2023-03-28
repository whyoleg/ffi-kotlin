package dev.whyoleg.foreign.memory

@Suppress("NO_ACTUAL_FOR_EXPECT")
@ForeignMemoryApi
public expect class MemoryAddress

//TODO: somehow hide those values...
@ForeignMemoryApi
public val MemoryHolder?._address: MemoryAddress get() = (this?.segment ?: MemorySegment.Empty).address
