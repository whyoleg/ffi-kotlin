@file:Suppress("DEPRECATION")

package dev.whyoleg.ffi

import kotlinx.cinterop.*

public actual class CInteropScope
@PublishedApi
internal constructor(
    private val memScope: MemScope,
) {

    private fun <T> alloc(type: kotlinx.cinterop.CVariable.Type): T {
        return interpretNullablePointed(memScope.alloc(type.size, type.align).rawPtr)!!
    }

    public actual fun <T : CVariable> alloc(type: CVariableType<T>): T {
        return alloc(type.type)
    }

    public actual fun <T : CVariable> alloc(type: CVariableType<T>, initialize: T.() -> Unit): T {
        return alloc(type).also(initialize)
    }

    @Suppress("ACTUAL_WITHOUT_EXPECT")
    public actual fun <T : CPointed> allocPointerTo(type: CPointedType<T>): CPointerVariable<T> {
        return alloc(CPointerVarOf)
    }

    public actual fun <T : CVariable> allocPointerTo(value: CValue<T>): CPointer<T> {
        return value.getPointer(memScope)
    }

    public actual fun <T : CVariable> allocArray(type: CVariableType<T>, size: Int): CArrayPointer<T> {
        return interpretCPointer(memScope.alloc(type.type.size * size, type.type.align).rawPtr)!!
    }

    public actual fun <T : CVariable> allocArrayOf(type: CVariableType<T>, vararg elements: CValue<T>): CArrayPointer<T> {
        val array = allocArray(type, elements.size)
        elements.forEachIndexed { index, element ->
            element.place(interpretCPointer(array.rawValue + index * type.type.size)!!)
        }
        return array
    }

    public actual fun alloc(value: Byte): ByteVariable {
        return alloc(ByteVariableType) { this.value = value }
    }

    public actual fun alloc(value: ULong): ULongVariable {
        return alloc(ULongVariableType) { this.value = value }
    }

    public actual fun alloc(value: String): CString {
        return value.cstr.getPointer(memScope)
    }

    public actual fun allocArrayOf(elements: ByteArray): CArrayPointer<ByteVariable> {
        return memScope.allocArrayOf(elements)
    }

    public actual fun <T> ByteArray.read(index: Int, block: (pointer: CArrayPointer<ByteVariable>, size: Int) -> T): T =
        pointed(index, block)

    public actual fun <T> ByteArray.write(index: Int, block: (pointer: CArrayPointer<ByteVariable>, size: Int) -> T): T =
        pointed(index, block)

    public actual fun <T> ByteArray.pointed(index: Int, block: (pointer: CArrayPointer<ByteVariable>, size: Int) -> T): T {
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
