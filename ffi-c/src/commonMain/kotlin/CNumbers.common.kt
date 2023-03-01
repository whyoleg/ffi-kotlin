package dev.whyoleg.ffi.c

import kotlin.jvm.*

//TODO: overall should be like this, but it's not possible to make it like this because of compatibility with cinterop
// may be later?
//public expect class ByteVariable : CVariable {
//    public var value: Byte
//
//    public companion object Type : CVariableType<ByteVariable>
//}

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class ByteVariable : CVariable
public expect object ByteVariableType : CVariableType<ByteVariable>

public expect var ByteVariable.value: Byte

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class UByteVariable : CVariable
public expect object UByteVariableType : CVariableType<UByteVariable>

public expect var UByteVariable.value: UByte

public expect fun CPointer<ByteVariable>.toUByte(): CPointer<UByteVariable>
public expect fun CPointer<UByteVariable>.toByte(): CPointer<ByteVariable>

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class IntVariable : CVariable
public expect object IntVariableType : CVariableType<IntVariable>

public expect var IntVariable.value: Int

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class UIntVariable : CVariable
public expect object UIntVariableType : CVariableType<UIntVariable>

public expect var UIntVariable.value: UInt

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class LongVariable : CVariable
public expect object LongVariableType : CVariableType<LongVariable>

public expect var LongVariable.value: Long

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class ULongVariable : CVariable
public expect object ULongVariableType : CVariableType<ULongVariable>

public expect var ULongVariable.value: ULong

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
