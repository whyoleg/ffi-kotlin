package dev.whyoleg.ffi

import kotlinx.cinterop.*

public actual class CInteropScope
@PublishedApi
internal constructor(
    private val memScope: MemScope,
) {
    public actual fun <T : CVariable> alloc(type: CVariableType<T>): T {
        return interpretNullablePointed(memScope.alloc(type.type.size, type.type.align).rawPtr)!!
    }

    public actual fun <T : CVariable> alloc(type: CVariableType<T>, initialize: T.() -> Unit): T {
        return alloc(type).also(initialize)
    }

    public actual fun alloc(value: Byte): CByteVariable {
        return alloc(CByteVariableType) { this.value = value }
    }

    public actual fun <T> ByteArray.read(index: Int, block: (pointer: CPointer<CByteVariable>, size: Int) -> T): T = pointed(index, block)
    public actual fun <T> ByteArray.write(index: Int, block: (pointer: CPointer<CByteVariable>, size: Int) -> T): T = pointed(index, block)
    public actual fun <T> ByteArray.pointed(index: Int, block: (pointer: CPointer<CByteVariable>, size: Int) -> T): T {
        val pointedSize = size - index
        check(pointedSize >= 0)
        return usePinned {
            block(it.addressOf(index), pointedSize)
        }
    }
}

public actual inline fun <T> cInteropScope(block: CInteropScope.() -> T): T {
    return memScoped {
        CInteropScope(this).block()
    }
}
