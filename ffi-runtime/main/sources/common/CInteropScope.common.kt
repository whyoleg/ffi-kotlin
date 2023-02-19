package dev.whyoleg.ffi

//TODO: context receiver + extension functions
//TODO: Name
public expect class CInteropScope {
    public fun <T : CVariable> alloc(type: CVariableType<T>): T
    public fun <T : CVariable> alloc(type: CVariableType<T>, initialize: T.() -> Unit): T
    public fun alloc(value: Byte): CByteVariable

    public fun <T> ByteArray.read(index: Int = 0, block: (pointer: CPointer<CByteVariable>, size: Int) -> T): T
    public fun <T> ByteArray.write(index: Int = 0, block: (pointer: CPointer<CByteVariable>, size: Int) -> T): T
    public fun <T> ByteArray.pointed(index: Int = 0, block: (pointer: CPointer<CByteVariable>, size: Int) -> T): T
}

public expect inline fun <T> cInteropScope(block: CInteropScope.() -> T): T

private fun test() = cInteropScope {
    val v = alloc(CByteVariableType) {
        this.value = 5
    }

    v.pointer
}
