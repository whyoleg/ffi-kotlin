package dev.whyoleg.ffi

//TODO: context receiver + extension functions
//TODO: Name
//TODO: rename alloc -> allocate
//TODO: allocPointer semantics
public expect class CInteropScope {
    //base
    public fun <T : CVariable> alloc(type: CVariableType<T>): T
    public fun <T : CVariable> alloc(type: CVariableType<T>, initialize: T.() -> Unit): T

    //pointers
    public fun allocPointer(): CPointerVariable<*>
    public fun <T : COpaque> allocPointerTo(): CPointerVariable<T>
    public fun <T : CVariable> allocPointerTo(type: CVariableType<T>): CPointerVariable<T>

    public fun <T : CVariable> allocPointerTo(value: CValue<T>): CPointer<T>

    //base array
    public fun <T : CVariable> allocArray(type: CVariableType<T>, size: Int): CArrayPointer<T>
    public fun <T : CVariable> allocArrayOf(type: CVariableType<T>, vararg elements: CValue<T>): CArrayPointer<T>

    //typed
    public fun alloc(value: Byte): CByteVariable
    public fun alloc(value: ULong): CULongVariable
    public fun alloc(value: String): CString

    //typed array - !COPY! array
    public fun allocArrayOf(elements: ByteArray): CArrayPointer<CByteVariable>

    //ByteArray helpers
    public fun <T> ByteArray.read(index: Int = 0, block: (pointer: CArrayPointer<CByteVariable>, size: Int) -> T): T
    public fun <T> ByteArray.write(index: Int = 0, block: (pointer: CArrayPointer<CByteVariable>, size: Int) -> T): T
    public fun <T> ByteArray.pointed(index: Int = 0, block: (pointer: CArrayPointer<CByteVariable>, size: Int) -> T): T
}

public expect inline fun <T> cInteropScope(block: CInteropScope.() -> T): T
