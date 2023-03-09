package dev.whyoleg.ffi.c

public actual typealias PlatformDependentInt = Long
public actual typealias PlatformDependentIntVariable = LongVariable
public actual typealias PlatformDependentIntVariableType = LongVariableType

public actual typealias PlatformDependentUInt = ULong
public actual typealias PlatformDependentUIntVariable = ULongVariable
public actual typealias PlatformDependentUIntVariableType = ULongVariableType

public actual val Int.pd: PlatformDependentInt get() = toLong()
public actual val UInt.pd: PlatformDependentUInt get() = toULong()

public actual var PlatformDependentIntVariable.pdValue: PlatformDependentInt
    get() = this.value
    set(value) = run { this.value = value }
public actual var PlatformDependentUIntVariable.pdValue: PlatformDependentUInt
    get() = this.value
    set(value) = run { this.value = value }
