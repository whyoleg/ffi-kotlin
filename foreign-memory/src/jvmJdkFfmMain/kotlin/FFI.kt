package dev.whyoleg.foreign.memory

import java.lang.foreign.*
import java.lang.invoke.*
import java.lang.foreign.MemoryLayout as JMemoryLayout

public object FFI {
    private val linker = Linker.nativeLinker()
    private val loaderLookup = SymbolLookup.loaderLookup()
    private val defaultLookup = linker.defaultLookup()
    private val lookup = SymbolLookup { loaderLookup.find(it).or { defaultLookup.find(it) } }

    public fun methodHandle(name: String, result: JMemoryLayout, args: Array<JMemoryLayout> = emptyArray()): MethodHandle =
        linker.downcallHandle(lookup.find(name).get(), FunctionDescriptor.of(result, *args))

    public fun methodHandle(name: String, args: Array<JMemoryLayout> = emptyArray()): MethodHandle =
        linker.downcallHandle(lookup.find(name).get(), FunctionDescriptor.ofVoid(*args))

    public val autoAllocator: SegmentAllocator get() = SegmentAllocator.nativeAllocator(SegmentScope.auto())
}
