@file:OptIn(ForeignMemoryApi::class)

package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*

internal actual typealias PlatformIntMemoryAccessor = LongMemoryAccessor

internal actual typealias PlatformUIntMemoryAccessor = ULongMemoryAccessor
