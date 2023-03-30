package dev.whyoleg.foreign.playground

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.lib.*
import dev.whyoleg.foreign.memory.*

internal fun testFunc(): Unit = memoryScoped {
    val b = allocatePointer(5.toByte())

    val bb by b

    val lengthVar: CPointer<Byte> = allocatePointer(5.toByte())
    val ptrVar: CPointer<CPointer<Byte>> = allocatePointer(lengthVar)

    var ptr: CPointer<Byte>? by ptrVar
    var length: Byte by lengthVar
    var ptrToPtrV: CPointer<Byte>? by ptrVar

    val paramPointer = returnPointer(5)!!

    var v by paramPointer

    usePointer(paramPointer)
    useValue(paramPointer.pointed)

    val paramValue = returnValue(4)
    val paramValuePointer = allocatePointer(paramValue)

    val s = allocatePointerFor(OSSL_PARAM)

    useValue(paramValue)
    usePointer(paramValuePointer)

    val test = allocatePointerFor(CType.Byte, 5)
    val test2 = allocatePointerFor(Byte)
    val test3 = allocatePointerFor(CType.Byte.pointer)

    val param = allocatePointer(OSSL_PARAM) {
        data_type = 123U
    }

    val para = allocateStruct(OSSL_PARAM) {

    }
}

internal fun usePointer(pointer: CPointer<OSSL_PARAM>?): Int = TODO()
internal fun useValue(value: OSSL_PARAM): Int = TODO()
internal fun returnPointer(value: Int): CPointer<OSSL_PARAM>? = TODO()
internal fun returnValue(value: Int): OSSL_PARAM = TODO()
