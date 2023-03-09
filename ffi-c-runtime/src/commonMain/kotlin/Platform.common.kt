package dev.whyoleg.ffi.c

import kotlin.jvm.*

//TODO: better naming?

public expect class PlatformInt : Number, Comparable<PlatformInt>
public expect class CPlatformInt internal constructor(memory: NativeMemory) : CPrimitive<PlatformInt> {
    override val type: Type

    public companion object Type : CPrimitive.Type<PlatformInt, CPlatformInt>
}

@JvmInline
public expect value class PlatformUInt internal constructor(internal val data: PlatformInt) :
    Comparable<PlatformUInt> {
    public fun toDouble(): Double
    public fun toFloat(): Float
    public fun toLong(): Long
    public fun toInt(): Int
    public fun toShort(): Short
    public fun toByte(): Byte
    //TODO: add unsigned
}

//@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class CPlatformUInt internal constructor(memory: NativeMemory) : CPrimitive<PlatformUInt> {
    override val type: Type

    public companion object Type : CPrimitive.Type<PlatformUInt, CPlatformUInt>
}

//
////TODO better approach?
//public expect val Int.pd: PlatformInt
//public expect val UInt.pd: PlatformDependentUInt
