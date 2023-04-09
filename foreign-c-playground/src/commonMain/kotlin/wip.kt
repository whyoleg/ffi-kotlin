package dev.whyoleg.foreign.playground

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.lib.*

internal fun testFunc(): Unit = foreignC {
    val b = cPointerOf(5.toByte())

    val bb by b

    val lengthVar: CPointer<Byte> = cPointerOf(5.toByte())
    val ptrVar: CPointer<CPointer<Byte>> = cPointerOf(lengthVar)

    var ptr: CPointer<Byte>? by ptrVar
    var length: Byte by lengthVar
    var ptrToPtrV: CPointer<Byte>? by ptrVar

    val paramPointer = returnPointer(5)!!

    var v by paramPointer

    usePointer(paramPointer)
    useValue(paramPointer.pointed)

    val paramValue = returnValue(4)
    val paramValuePointer = cPointerOf(paramValue)

    val s = cPointerOf(OSSL_PARAM)

    val array = cArrayOf(OSSL_PARAM) {
        add(OSSL_PARAM_construct_end())
    }

    useValue(paramValue)
    usePointer(paramValuePointer)

    val test = cPointerOf(CType.Byte, 5)
    val test2 = cPointerOf(Byte)

    val tt = cPointerOf(CType.PlatformUInt)
    val test3 = cPointerOf(CType.Byte.pointer)

    val param = cPointerOf(OSSL_PARAM) {
        data_type = 123U
    }

    val para = cStructOf(OSSL_PARAM) {

    }
}

internal fun usePointer(pointer: CPointer<OSSL_PARAM>?): Int = TODO()
internal fun useValue(value: OSSL_PARAM): Int = TODO()
internal fun returnPointer(value: Int): CPointer<OSSL_PARAM>? = TODO()
internal fun returnValue(value: Int): OSSL_PARAM = TODO()
