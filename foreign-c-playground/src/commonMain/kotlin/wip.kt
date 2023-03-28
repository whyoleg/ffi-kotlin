package dev.whyoleg.foreign.playground

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.lib.*
import dev.whyoleg.foreign.memory.*

internal fun testFunc(): Unit = memoryScoped {
    val b = pointer(5.toByte())

    val bb by b

    val lengthVar: CPointer<Byte> = pointer(5.toByte())
    val ptrVar: CPointer<CPointer<Byte>> = pointer(lengthVar)

    var ptr: CPointer<Byte>? by ptrVar
    var length: Byte by lengthVar
    var ptrToPtrV: CPointer<Byte>? by ptrVar

    val paramPointer = returnPointer(5)!!

    var v by paramPointer

    usePointer(paramPointer)
    useValue(paramPointer.value!!)

    val paramValue = returnValue(4)
    val paramValuePointer = pointer(paramValue)

    val s = pointerFor(OSSL_PARAM)

    useValue(paramValue)
    usePointer(paramValuePointer)

    val test = pointerFor(CType.Byte, 5)
    val test2 = pointerFor(Byte)
    val test3 = pointerFor(CType.Byte.pointer)

    val param = pointer(OSSL_PARAM) {
        data_type = 123U
    }

    val para = struct(OSSL_PARAM) {

    }
}

internal fun usePointer(pointer: CPointer<OSSL_PARAM>?): Int = TODO()
internal fun useValue(value: OSSL_PARAM): Int = TODO()
internal fun returnPointer(value: Int): CPointer<OSSL_PARAM>? = TODO()
internal fun returnValue(value: Int): OSSL_PARAM = TODO()
