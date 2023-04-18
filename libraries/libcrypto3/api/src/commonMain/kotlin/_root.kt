package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.memory.*

//TODO: from where to get this name?
expect val ForeignMemory.Companion.LibCrypto3: ForeignMemory

internal val libCrypto3ImplicitScope get() = ForeignCScope.implicit(ForeignMemory.LibCrypto3)
