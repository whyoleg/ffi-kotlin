package dev.whyoleg.ffi

public actual class CInteropScope
@PublishedApi
internal constructor(
    private val allocator: NativeAllocator,
) {

    public actual fun <T : CVariable> alloc(type: CVariableType<T>): T {
        return type.wrap(allocator.allocate(type.byteSize))
    }

    public actual fun <T : CVariable> alloc(type: CVariableType<T>, initialize: T.() -> Unit): T {
        return alloc(type).apply(initialize)
    }

    public actual fun <T : CPointed> allocPointerTo(type: CPointedType<T>): CPointerVariable<T> {
        return CPointerVariable(allocator.allocate(pointerSize), type)
    }

    public actual fun <T : CVariable> allocPointerTo(value: CValue<T>): CPointer<T> {
        return CPointer(allocator.allocate(value.type.byteSize).also(value.memory::copyTo), value.type)
    }

    public actual fun <T : CVariable> allocArray(type: CVariableType<T>, size: Int): CArrayPointer<T> {
        return CPointer(allocator.allocate(type.byteSize * size), type)
    }

    public actual fun <T : CVariable> allocArrayOf(type: CVariableType<T>, vararg elements: CValue<T>): CArrayPointer<T> {
        val array = allocArray(type, elements.size)
        elements.forEachIndexed { index, element ->
            element.memory.copyTo(array.memory, index * type.byteSize, type.byteSize)
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
        val bytes = value.encodeToByteArray()
        return CPointer(allocator.allocate(bytes.size + 1).also {
            it.storeByteArray(0, bytes)
            it.storeByte(bytes.size, 0)
        }, CByteVariableType)
    }

    public actual fun allocArrayOf(elements: ByteArray): CArrayPointer<CByteVariable> {
        return CPointer(allocator.allocate(elements.size).also { it.storeByteArray(0, elements) }, CByteVariableType)
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
        val memory = allocator.allocate(pointedSize)
        if (copyBefore) memory.storeByteArray(0, this, index, pointedSize)
        val result = block(CPointer(memory, CByteVariableType), pointedSize)
        if (copyAfter) memory.loadByteArray(0, this, index, pointedSize)
        return result
    }
}

public actual inline fun <T> cInteropScope(block: CInteropScope.() -> T): T {
    val allocator = NativeAllocator.Default()
    try {
        return CInteropScope(allocator).block()
    } finally {
        allocator.close()
    }
}
