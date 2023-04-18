package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.memory.*
import org.khronos.webgl.*

private val memory = ForeignMemory(LibCrypto3WasmMemory)

actual val ForeignMemory.Companion.LibCrypto3: ForeignMemory get() = memory

private object LibCrypto3WasmMemory : WasmMemory() {
    override fun malloc(size: MemoryAddressSize): MemoryAddress = ffi.asm.malloc(size)
    override fun free(address: MemoryAddress): Unit = ffi.asm.free(address)
    override fun loadByte(address: MemoryAddressSize): Byte {
        return ffi.HEAPU8[address]
    }

    override fun storeByte(address: MemoryAddressSize, value: Byte) {
        ffi.HEAPU8[address] = value
    }

    override fun loadInt(address: MemoryAddressSize): Int {
        return ffi.HEAPU32[address / Int.SIZE_BYTES]
    }

    override fun storeInt(address: MemoryAddressSize, value: Int) {
        ffi.HEAPU32[address / Int.SIZE_BYTES] = value
    }
}

@JsModule("foreign-crypto-wasm")
@JsNonModule
@JsName("Module")
private external object ffi {
    val HEAPU8: Uint8Array
    val HEAPU32: Uint32Array

    object asm {
        fun malloc(size: Int): Int
        fun free(pointer: Int)
    }
}

@Suppress("DEPRECATION")
@OptIn(ExperimentalStdlibApi::class, ExperimentalJsExport::class)
@EagerInitialization
@JsExport
@Deprecated("", level = DeprecationLevel.HIDDEN)
public val initHook: dynamic = WasmMemory.register(LibCrypto3WasmMemory)
