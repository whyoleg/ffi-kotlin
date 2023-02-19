package dev.whyoleg.ffi

import java.lang.foreign.*
import java.lang.invoke.*

public abstract class FFI {
    public fun methodHandle(name: String, result: MemoryLayout, args: Array<MemoryLayout> = emptyArray()): MethodHandle =
        linker.downcallHandle(lookup.find(name).get(), FunctionDescriptor.of(result, *args))

    public fun methodHandle(name: String, args: Array<MemoryLayout> = emptyArray()): MethodHandle =
        linker.downcallHandle(lookup.find(name).get(), FunctionDescriptor.ofVoid(*args))

    //TODO: decide better on how to handle structures allocation!!!
    // should be `get` to create new scope for every function call
    public val autoSegmentAllocator: SegmentAllocator get() = SegmentAllocator.nativeAllocator(SegmentScope.auto())

    private companion object {
        private val linker = Linker.nativeLinker()
        private val loaderLookup = SymbolLookup.loaderLookup()
        private val defaultLookup = linker.defaultLookup()
        private val lookup = SymbolLookup { loaderLookup.find(it).or { defaultLookup.find(it) } }
    }
}
