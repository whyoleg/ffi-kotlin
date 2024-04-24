package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*

@Suppress("UNUSED_VARIABLE", "DEPRECATION")
private fun MemoryScope.test() {
    val b0: CPointer<Byte> = allocate(CType.Builtin.Byte)
    val b1 = allocate(Byte)
    val b2 = allocateFrom(Byte, 42.toByte())
    val b3 = allocateFrom(42.toByte())

    val bv = b0.value

//    cTypeOf<Byte>()
//    cTypeOf<CPointer<OSSL_SOMETHING>?>()


    val c0 = allocate(CPointer(CType.Builtin.Byte))
    val c1 = allocate(CPointer(Byte))
    val c2 = allocateFrom(CPointer(Byte), b0)
    val c3 = allocateFrom(b0)

    val cv = c0.value

    val cc0 = allocate(CPointer(CPointer(CType.Builtin.Byte)))
    val cc1 = allocate(CPointer(CPointer(Byte)))
    val cc2 = allocateFrom(CPointer(CPointer(Byte)), c0)
    val cc3 = allocateFrom(c0)

    val ccv = cc0.value

    val dumbStruct = OSSL_SOMETHING()
    val dumbStruct2 = OSSL_SOMETHING { data_type = 1u }

    val s0 = allocate(OSSL_SOMETHING)
    val s1 = allocate(OSSL_SOMETHING)
    val s2 = allocateFrom(OSSL_SOMETHING, dumbStruct)
    val s3 = allocateFrom(dumbStruct)
    val s4 = allocateFrom(OSSL_SOMETHING { data_type = 5u })
    val s5 = allocateFrom(OSSL_SOMETHING())

    val sv = s0.value

    val sc0 = allocate(CPointer(OSSL_SOMETHING))
    val sc1 = allocate(CPointer(OSSL_SOMETHING))
    val sc2 = allocateFrom(CPointer(OSSL_SOMETHING), s0)
    val sc3 = allocateFrom(s0)

    val scv = sc0.value

    val o0 = allocate(SOME_OP)
    val o1 = allocate(SOME_OP)
    val o2 = allocateFrom(SOME_OP, o0.value)
    val o3 = allocateFrom(o0.value)

    val ov = o0.value

    val oc0 = allocate(CPointer(SOME_OP))
    val oc1 = allocate(CPointer(SOME_OP))
    val oc2 = allocateFrom(CPointer(SOME_OP), o0)
    val oc3 = allocateFrom(o0)

    val ocv = oc0.value

    val a0 = allocateArray(CType.Builtin.Byte, 10)
    val a1 = allocateArray(Byte, 10)
    val a2 = allocateArrayFrom(Byte, ByteArray(10))
    val a3 = allocateArrayFrom(ByteArray(10))

    val av = a0.value
    val avc = a0[0]
    val avcv = avc.value
    a0.forEach { ptr ->

    }

    val ac0 = allocateArray(CPointer(CType.Builtin.Byte), 10)
    val ac1 = allocateArray(CPointer(Byte), 10)
    val ac2 = allocateArrayFrom(CPointer(Byte), listOf(b0))
//    val ac3 = allocateArrayFrom(listOf(b0))

    val acv = ac0.value
    val acvc = ac0[0]
    val acvcv = acvc.value
    ac0.forEach { ptr ->

    }

    val as0 = allocateArray(OSSL_SOMETHING, 10)
    val as1 = allocateArray(OSSL_SOMETHING, 10)
    val as2 = allocateArrayFrom(OSSL_SOMETHING, listOf(OSSL_SOMETHING()))
//    val as3 = allocateArrayFrom(listOf(OSSL_SOMETHING()))

    val asv = as0.value
    val asvc = as0[0]
    val asvcv = asvc.value
    as0.forEach { ptr ->

    }
}

private class SOME_OP : COpaque {
    companion object : CType.Opaque<SOME_OP>
}

private class OSSL_SOMETHING : CStruct {
    var key: CString?
        get() = TODO()
        set(value) = TODO()
    var data_type: UInt
        get() = TODO()
        set(value) = TODO()
    var data: CPointer<Unit>?
        get() = TODO()
        set(value) = TODO()
    var data_size: PlatformUInt
        get() = TODO()
        set(value) = TODO()
    var return_size: PlatformUInt
        get() = TODO()
        set(value) = TODO()

    companion object : CType.Record<OSSL_SOMETHING>
}

private inline fun MemoryScope.OSSL_SOMETHING(block: OSSL_SOMETHING.() -> Unit = {}): OSSL_SOMETHING = TODO()
