package dev.whyoleg.ffi.c

public actual class CInteropScope
@PublishedApi
internal constructor(
    private val allocator: NativeAllocator,
) {

    public actual fun <T : CVariable> alloc(type: CVariableType<T>): T {
        return type.wrap(allocator.allocate(type.layout))
    }

    public actual fun <T : CVariable> alloc(type: CVariableType<T>, initialize: T.() -> Unit): T {
        return alloc(type).apply(initialize)
    }

    public actual fun <T : CPointed> allocPointerTo(type: CPointedType<T>): CPointerVariable<T> {
        return CPointerVariable(type, allocator.allocate(NativeLayout.Pointer))
    }

    public actual fun <T : CVariable> allocPointerTo(value: CValue<T>): CPointer<T> {
        val memory = allocator.allocate(value.type.layout).also {
            value.memory.copyElementTo(0, value.type.layout, 0, it)
        }
        return CPointer(value.type, memory.pointer)
    }

    public actual fun <T : CVariable> allocArray(type: CVariableType<T>, size: Int): CArrayPointer<T> {
        return CPointer(type, allocator.allocateArray(type.layout, size).pointer)
    }

    public actual fun <T : CVariable> allocArrayOf(type: CVariableType<T>, vararg elements: CValue<T>): CArrayPointer<T> {
        val array = allocator.allocateArray(type.layout, elements.size)
        elements.forEachIndexed { index, element ->
            element.memory.copyElementTo(index, type.layout, 0, array)
        }
        return CPointer(type, array.pointer)
    }

    public actual fun alloc(value: Byte): ByteVariable {
        return alloc(ByteVariableType) { this.value = value }
    }

    public actual fun alloc(value: ULong): ULongVariable {
        return alloc(ULongVariableType) { this.value = value }
    }

    public actual fun alloc(value: String): CString {
        return CPointer(ByteVariableType, allocator.allocateString(value).pointer)
    }

    public actual fun allocArrayOf(elements: ByteArray): CArrayPointer<ByteVariable> {
        return CPointer(ByteVariableType, allocator.allocateArray(NativeLayout.Byte, elements.size).also {
            it.storeByteArray(0, elements)
        }.pointer)
    }

    public actual fun <T> ByteArray.read(index: Int, block: (pointer: CArrayPointer<ByteVariable>, size: Int) -> T): T =
        use(index, copyBefore = true, block = block)

    public actual fun <T> ByteArray.write(index: Int, block: (pointer: CArrayPointer<ByteVariable>, size: Int) -> T): T =
        use(index, copyAfter = true, block = block)

    public actual fun <T> ByteArray.pointed(index: Int, block: (pointer: CArrayPointer<ByteVariable>, size: Int) -> T): T =
        use(index, copyBefore = true, copyAfter = true, block = block)

    private fun <T> ByteArray.use(
        index: Int,
        copyBefore: Boolean = false,
        copyAfter: Boolean = false,
        block: (pointer: CPointer<ByteVariable>, size: Int) -> T,
    ): T = createDefaultNativeAllocator().use {
        val pointedSize = size - index
        check(pointedSize >= 0)
        val memory = it.allocateArray(NativeLayout.Byte, pointedSize)
        if (copyBefore) memory.storeByteArray(0, this, index, pointedSize)
        val result = block(CPointer(ByteVariableType, memory.pointer), pointedSize)
        if (copyAfter) memory.loadByteArray(0, this, index, pointedSize)
        return result
    }
}

public actual inline fun <T> cInteropScope(block: CInteropScope.() -> T): T =
    createDefaultNativeAllocator().use { CInteropScope(it).block() }
