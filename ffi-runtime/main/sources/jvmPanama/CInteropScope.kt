package dev.whyoleg.ffi

import java.lang.foreign.*

public actual class CInteropScope
@PublishedApi
internal constructor(
    public val arena: Arena,
) {

    public actual fun <T : CVariable> alloc(type: CVariableType<T>): T {
        return type.allocate(arena)
    }

    public actual fun <T : CVariable> alloc(type: CVariableType<T>, initialize: T.() -> Unit): T {
        return alloc(type).apply(initialize)
    }

    public actual fun alloc(value: Byte): CByteVariable {
        return alloc(CByteVariableType) { this.value = value }
    }

    public actual fun <T> ByteArray.read(index: Int, block: (pointer: CPointer<CByteVariable>, size: Int) -> T): T =
        use(index, copyBefore = true, block = block)

    public actual fun <T> ByteArray.write(index: Int, block: (pointer: CPointer<CByteVariable>, size: Int) -> T): T =
        use(index, copyAfter = true, block = block)

    public actual fun <T> ByteArray.pointed(index: Int, block: (pointer: CPointer<CByteVariable>, size: Int) -> T): T =
        use(index, copyBefore = true, copyAfter = true, block = block)

    private fun <T> ByteArray.use(
        index: Int,
        copyBefore: Boolean = false,
        copyAfter: Boolean = false,
        block: (pointer: CPointer<CByteVariable>, size: Int) -> T,
    ): T {
        val pointedSize = size - index
        check(pointedSize >= 0)
        return Arena.openConfined().use {
            val array = MemorySegment.ofArray(this).asSlice(index.toLong())
            val segment = it.allocate(pointedSize.toLong())
            if (copyBefore) segment.copyFrom(array)
            val result = block(CPointer(CByteVariable(segment)), size)
            if (copyAfter) array.copyFrom(segment)
            result
        }
    }
}

public actual inline fun <T> cInteropScope(block: CInteropScope.() -> T): T {
    return Arena.openConfined().use {
        CInteropScope(it).block()
    }
}
