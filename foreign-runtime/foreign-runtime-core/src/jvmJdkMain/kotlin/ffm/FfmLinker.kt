package dev.whyoleg.foreign.ffm

import dev.whyoleg.foreign.memory.*
import java.lang.foreign.*
import java.lang.invoke.*
import kotlin.jvm.optionals.*

//TODO: rename?
@ForeignMemoryApi
public object FfmLinker {
    private val linker = Linker.nativeLinker()
    private val loaderLookup = SymbolLookup.loaderLookup()

    private fun get(name: String): MemorySegment {
        return loaderLookup.find(name).getOrNull()
            ?: linker.defaultLookup().find(name).getOrNull()
            ?: error("No symbol found: $name")
    }

    public fun methodHandle(name: String, result: MemoryLayout, args: Array<MemoryLayout> = emptyArray()): MethodHandle =
        linker.downcallHandle(get(name), FunctionDescriptor.of(result, *args))

    public fun methodHandle(name: String, args: Array<MemoryLayout> = emptyArray()): MethodHandle =
        linker.downcallHandle(get(name), FunctionDescriptor.ofVoid(*args))
}

@ForeignMemoryApi
public fun MemoryArena.ffmSegmentAllocator(): SegmentAllocator = when (this) {
    is FfmMemoryArena -> allocator
    else              -> TODO("should not happen?")
}

@ForeignMemoryApi
public fun MemoryBlock.ffmMemorySegment(): MemorySegment = when (this) {
    is FfmMemoryBlock -> segment
    else              -> TODO("should not happen?") //MemorySegment.ofAddress(address, size)
}

@ForeignMemoryApi
public fun MemoryBlockLayout.ffmMemoryLayout(): MemoryLayout = when (this) {
    is FfmMemoryBlockLayout       -> layout
    is FfmMemoryBlockLayoutRecord -> layout
    else                          -> TODO("should not happen?")
}
