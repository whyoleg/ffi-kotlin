@file:OptIn(ForeignMemoryApi::class)

package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*

internal actual typealias PlatformIntMemoryAccessor = IntMemoryAccessor

internal actual typealias PlatformUIntMemoryAccessor = UIntMemoryAccessor
