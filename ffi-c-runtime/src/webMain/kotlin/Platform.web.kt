package dev.whyoleg.ffi.c

public actual typealias PlatformDependentInt = Int
public actual typealias PlatformDependentIntVariable = IntVariable
public actual typealias PlatformDependentIntVariableType = IntVariableType

public actual typealias PlatformDependentUInt = UInt
public actual typealias PlatformDependentUIntVariable = UIntVariable
public actual typealias PlatformDependentUIntVariableType = UIntVariableType

public actual val Int.pd: PlatformDependentInt get() = this
public actual val UInt.pd: PlatformDependentUInt get() = this

public actual var PlatformDependentIntVariable.pdValue: PlatformDependentInt
    get() = this.value
    set(value) = run { this.value = value }
public actual var PlatformDependentUIntVariable.pdValue: PlatformDependentUInt
    get() = this.value
    set(value) = run { this.value = value }
