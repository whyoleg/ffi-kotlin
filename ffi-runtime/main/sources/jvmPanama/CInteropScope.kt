package dev.whyoleg.ffi

import java.lang.foreign.*

public actual class CInteropScope
@PublishedApi
internal constructor(
    public val arena: Arena,
) {

    public actual fun <T : CVariable> alloc(type: CVariableType<T>): T {
        return type.wrap(arena.allocate(type.layout))
    }

    public actual fun <T : CVariable> alloc(type: CVariableType<T>, initialize: T.() -> Unit): T {
        return alloc(type).apply(initialize)
    }

    public actual fun allocPointer(): CPointerVariable<*> {
        return allocPointerTo<COpaque>()
    }

    public actual fun <T : COpaque> allocPointerTo(): CPointerVariable<T> {
        return CPointerVariable(::COpaqueImpl, arena.allocate(ValueLayout.ADDRESS)) as CPointerVariable<T>
    }

    public actual fun <T : CVariable> allocPointerTo(type: CVariableType<T>): CPointerVariable<T> {
        return CPointerVariable(type.wrap, arena.allocate(ValueLayout.ADDRESS))
    }

    public actual fun <T : CVariable> allocPointerTo(value: CValue<T>): CPointer<T> {
        return CPointer(alloc(value.type) { segment.copyFrom(value.segment) })
    }

    public actual fun <T : CVariable> allocArray(type: CVariableType<T>, size: Int): CArrayPointer<T> {
        return CPointer(type.wrap(arena.allocateArray(type.layout, size.toLong())))
    }

    public actual fun <T : CVariable> allocArrayOf(type: CVariableType<T>, vararg elements: CValue<T>): CArrayPointer<T> {
        val array = allocArray(type, elements.size)
        elements.forEachIndexed { index, element ->
            MemorySegment.copy(
                element.segment, 0L,
                array.segment, index * type.layout.byteSize(), type.layout.byteSize()
            )
        }
        return array
    }

    public actual fun alloc(value: Byte): CByteVariable {
        return alloc(CByteVariableType) { this.value = value }
    }

    public actual fun alloc(value: ULong): CULongVariable {
        return alloc(CULongVariableType) { this.value = value }
    }

    public actual fun alloc(value: String): CString {
        return CPointer(CByteVariable(arena.allocateUtf8String(value)))
    }

    public actual fun allocArrayOf(elements: ByteArray): CArrayPointer<CByteVariable> {
        return CPointer(CByteVariable(arena.allocateArray(ValueLayout.JAVA_BYTE, *elements)))
    }

    public actual fun <T> ByteArray.read(index: Int, block: (pointer: CArrayPointer<CByteVariable>, size: Int) -> T): T =
        use(index, copyBefore = true, block = block)

    public actual fun <T> ByteArray.write(index: Int, block: (pointer: CArrayPointer<CByteVariable>, size: Int) -> T): T =
        use(index, copyAfter = true, block = block)

    public actual fun <T> ByteArray.pointed(index: Int, block: (pointer: CArrayPointer<CByteVariable>, size: Int) -> T): T =
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
            //TODO: may be just use MemorySegment.allocateArray?
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
