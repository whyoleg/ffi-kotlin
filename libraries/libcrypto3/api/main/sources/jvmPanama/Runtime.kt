package dev.whyoleg.ffi.libcrypto3

import java.lang.foreign.*
import java.lang.invoke.*
import java.util.*

internal object Runtime : SymbolLookup {
    private val linker = Linker.nativeLinker()
    private val loaderLookup = SymbolLookup.loaderLookup()
    private val defaultLookup = linker.defaultLookup()

    init {
        System.loadLibrary("crypto")
    }

    override fun find(name: String): Optional<MemorySegment> {
        return loaderLookup.find(name).or { defaultLookup.find(name) }
    }

    fun methodHandle(name: String, result: MemoryLayout, args: Array<MemoryLayout> = emptyArray()): MethodHandle =
        linker.downcallHandle(find(name).get(), FunctionDescriptor.of(result, *args))

    fun methodHandle(name: String, args: Array<MemoryLayout> = emptyArray()): MethodHandle =
        linker.downcallHandle(find(name).get(), FunctionDescriptor.ofVoid(*args))
}
