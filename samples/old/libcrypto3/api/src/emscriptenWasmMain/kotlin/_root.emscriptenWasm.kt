package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.memory.*

private val memory = ForeignMemory(LibCrypto3WasmMemory)

actual val ForeignMemory.Companion.LibCrypto3: ForeignMemory get() = memory

private object LibCrypto3WasmMemory : WasmMemory() {
    override fun malloc(size: MemoryAddressSize): MemoryAddress = _malloc(size)
    override fun free(address: MemoryAddress): Unit = _free(address)
    override fun loadByte(address: MemoryAddressSize): Byte {
        return _getU8(address)
    }

    override fun storeByte(address: MemoryAddressSize, value: Byte) {
        _setU8(address, value)
    }

    override fun loadInt(address: MemoryAddressSize): Int {
        return _getU32(address / Int.SIZE_BYTES)
    }

    override fun storeInt(address: MemoryAddressSize, value: Int) {
        _setU32(address / Int.SIZE_BYTES, value)
    }
}

//TODO: those are really super dependent - need to check on how to provide it better
@JsFun("(index) => imports.Module.HEAPU8[index]")
private external fun _getU8(index: Int): Byte

@JsFun("(index, value) => imports.Module.HEAPU8[index] = value")
private external fun _setU8(index: Int, value: Byte)

@JsFun("(index) => imports.Module.HEAPU32[index]")
private external fun _getU32(index: Int): Int

@JsFun("(index, value) => imports.Module.HEAPU32[index] = value")
private external fun _setU32(index: Int, value: Int)

@JsFun("(size) => imports.Module.asm.malloc(size)")
private external fun _malloc(size: Int): Int

@JsFun("(pointer) => imports.Module.asm.free(pointer)")
private external fun _free(pointer: Int)

@Suppress("DEPRECATION")
@OptIn(ExperimentalStdlibApi::class)
@EagerInitialization
private val initHook: Unit = WasmMemory.register(LibCrypto3WasmMemory)
