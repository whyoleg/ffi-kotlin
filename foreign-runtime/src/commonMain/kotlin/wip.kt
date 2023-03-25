package dev.whyoleg.foreign

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.memory.*

internal fun MemoryScope.testFunc() {
    val lengthVar: CPointer<Int> = pointer(5)
    val ptrVar: CPointer<CPointer<Int>?> = pointerTo(lengthVar)

    var ptr: CPointer<Int>? by ptrVar
    var length: Int by lengthVar
    var ptrToPtrV: CPointer<Int>? by ptrVar

    val paramPointer = returnPointer(5)

    val r = CType.Int

    var v by paramPointer

    usePointer(paramPointer)
    useValue(paramPointer.value!!)

    val paramValue = returnValue(4)
    val paramValuePointer = pointerFor(OSSL_PARAM, paramValue)

    val s = pointerFor(OSSL_PARAM)

    useValue(paramValue)
    usePointer(paramValuePointer)

    val test = pointerFor(CType.Int)
    val test2 = pointerFor(Int)

    val param = pointer(OSSL_PARAM) {
        data_type = 123U
    }

    val para = struct(OSSL_PARAM) {

    }
}

internal fun MemoryScope.usePointer(pointer: CPointer<out OSSL_PARAM?>?): Int = TODO()
internal fun MemoryScope.useValue(value: OSSL_PARAM): Int = TODO()
internal fun MemoryScope.returnPointer(value: Int): CPointer<OSSL_PARAM?> = TODO()
internal fun MemoryScope.returnValue(value: Int): OSSL_PARAM = TODO()
