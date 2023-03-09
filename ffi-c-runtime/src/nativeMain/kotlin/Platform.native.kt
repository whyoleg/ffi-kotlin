@file:Suppress(
    "ACTUAL_WITHOUT_EXPECT",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
)

package dev.whyoleg.ffi.c

import kotlinx.cinterop.*
import platform.posix.*

//we should size_t/ssize_t to make it possible to use commonization of bit width
public actual typealias PlatformDependentInt = ssize_t
public actual typealias PlatformDependentIntVariable = ssize_tVar

public actual typealias PlatformDependentUInt = size_t
public actual typealias PlatformDependentUIntVariable = size_tVar

public actual val Int.pd: PlatformDependentInt get() = convert()
public actual val UInt.pd: PlatformDependentUInt get() = convert()

public actual var PlatformDependentIntVariable.pdValue: PlatformDependentInt
    get() = this.value
    set(value) = run { this.value = value }
public actual var PlatformDependentUIntVariable.pdValue: PlatformDependentUInt
    get() = this.value
    set(value) = run { this.value = value }
