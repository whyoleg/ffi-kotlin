package dev.whyoleg.ffi.c

import java.lang.foreign.*
import java.lang.invoke.*

public object FFI {
    private val linker = Linker.nativeLinker()
    private val loaderLookup = SymbolLookup.loaderLookup()
    private val defaultLookup = linker.defaultLookup()
    private val lookup = SymbolLookup { loaderLookup.find(it).or { defaultLookup.find(it) } }

    init {
        loadLibraries()
    }

    public fun methodHandle(name: String, result: MemoryLayout, args: Array<MemoryLayout> = emptyArray()): MethodHandle =
        linker.downcallHandle(lookup.find(name).get(), FunctionDescriptor.of(result, *args))

    public fun methodHandle(name: String, args: Array<MemoryLayout> = emptyArray()): MethodHandle =
        linker.downcallHandle(lookup.find(name).get(), FunctionDescriptor.ofVoid(*args))

}
