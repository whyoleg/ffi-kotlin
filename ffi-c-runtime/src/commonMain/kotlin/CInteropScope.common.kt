package dev.whyoleg.ffi.c

//TODO: context receiver + extension functions
//TODO: Name
//TODO: rename alloc -> allocate
//TODO: allocPointer semantics
public class CInteropScope
@PublishedApi
internal constructor(
    private val allocator: NativeAllocator,
) {
    //base
    public fun <T : CVariable> alloc(type: CVariable.Type<T>): T {
        return type.wrap(allocator.allocate(type.layout))
    }

    public fun <T : CVariable> alloc(type: CVariable.Type<T>, initialize: T.() -> Unit): T {
        return alloc(type).apply(initialize)
    }

    //pointers: TODO: naming...
    public fun <T : CPointed> allocPointerTo(type: CPointed.Type<T>): CPointerVariable<T> {
        return CPointerVariable(type, allocator.allocate(NativeLayout.Pointer))
    }
//    public fun <T : CVariable> allocPointerTo(value: CValue<T>): CPointer<T>

    //base array
    public fun <T : CVariable> allocArray(type: CVariable.Type<T>, size: Int): CArrayPointer<T> {
        return CPointer(type, allocator.allocateArray(type.layout, size).pointer)
    }
//    public fun <T : CVariable> allocArrayOf(type: CVariable.Type<T>, vararg elements: CValue<T>): CArrayPointer<T>

    //typed
    public fun alloc(value: Byte): CByte = alloc(CByte) { this.value = value }
    public fun alloc(value: ULong): CULong = alloc(CULong) { this.value = value }
    public fun alloc(value: String): CString {
        return CPointer(CByte, allocator.allocateString(value).pointer)
    }

    //typed array - !COPY! array
    public fun allocArrayOf(elements: ByteArray): CArrayPointer<CByte> {
        return CPointer(CByte, allocator.allocateArray(NativeLayout.Byte, elements.size).also {
            it.storeByteArray(0, elements)
        }.pointer)
    }

    //ByteArray helpers
    public fun <T> ByteArray.read(index: Int = 0, block: (pointer: CArrayPointer<CByte>, size: Int) -> T): T =
        use(index, copyBefore = true, block = block)

    public fun <T> ByteArray.write(index: Int = 0, block: (pointer: CArrayPointer<CByte>, size: Int) -> T): T =
        use(index, copyAfter = true, block = block)

    public fun <T> ByteArray.pointed(index: Int = 0, block: (pointer: CArrayPointer<CByte>, size: Int) -> T): T =
        use(index, copyBefore = true, copyAfter = true, block = block)


    private fun <T> ByteArray.use(
        index: Int,
        copyBefore: Boolean = false,
        copyAfter: Boolean = false,
        block: (pointer: CPointer<CByte>, size: Int) -> T,
    ): T = createDefaultNativeAllocator().use {
        val pointedSize = size - index
        check(pointedSize >= 0)
        val memory = it.allocateArray(NativeLayout.Byte, pointedSize)
        if (copyBefore) memory.storeByteArray(0, this, index, pointedSize)
        val result = block(CPointer(CByte, memory.pointer), pointedSize)
        if (copyAfter) memory.loadByteArray(0, this, index, pointedSize)
        return result
    }
}

public inline fun <T> cInteropScope(block: CInteropScope.() -> T): T =
    createDefaultNativeAllocator().use { CInteropScope(it).block() }
