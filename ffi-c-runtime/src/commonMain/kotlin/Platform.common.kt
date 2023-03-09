package dev.whyoleg.ffi.c

import kotlin.jvm.*

public expect class PlatformDependentInt : Number, Comparable<PlatformDependentInt>

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class PlatformDependentIntVariable : CVariable
public expect object PlatformDependentIntVariableType : CVariableType<PlatformDependentIntVariable>

@JvmInline
public expect value class PlatformDependentUInt internal constructor(internal val data: PlatformDependentInt) :
    Comparable<PlatformDependentUInt> {
    public fun toDouble(): Double
    public fun toFloat(): Float
    public fun toLong(): Long
    public fun toInt(): Int
    public fun toShort(): Short
    public fun toByte(): Byte
    //TODO: add unsigned
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class PlatformDependentUIntVariable : CVariable
public expect object PlatformDependentUIntVariableType : CVariableType<PlatformDependentUIntVariable>


//TODO better approach?
public expect val Int.pd: PlatformDependentInt
public expect val UInt.pd: PlatformDependentUInt

//TODO: value will clash in platform dependent sourceSets...
public expect var PlatformDependentIntVariable.pdValue: PlatformDependentInt
public expect var PlatformDependentUIntVariable.pdValue: PlatformDependentUInt
