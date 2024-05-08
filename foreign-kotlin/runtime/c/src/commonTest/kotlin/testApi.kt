package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*
import kotlin.jvm.*

@Suppress("UNUSED_VARIABLE", "DEPRECATION")
private fun MemoryScope.test() {
    // investigate
//    val pointers = object {
//        val b by cPointer(0)
//    }
//    val ii = pointers::b
    val someByte = 123.toByte()
    val b0 = allocateCPointer(CType.Byte)
    val b1 = allocateCPointer(Byte)
    val b2 = allocateCPointer(Byte, 42.toByte())
    val b3 = allocateCPointer(42.toByte())
    val b4 = allocateCPointer(someByte)

    var b00 by b0

    val bv = b0.pointed

//    cTypeOf<Byte>()
//    cTypeOf<CPointer<OSSL_SOMETHING>?>()


    val c0 = allocateCPointer(CType.CPointer(CType.Byte))
    val cx = allocateCPointer(CPointer(CType.Byte))
    val c1 = allocateCPointer(CPointer(Byte))
    val c2 = allocateCPointer(CPointer(Byte), b0)
//    val c3 = cPointerFrom(b0)

    val cv = c0.pointed


    val cc0 = allocateCPointer(CPointer(CPointer(CType.Byte)))
    val cc1 = allocateCPointer(CPointer(CPointer(Byte)))
    val cc2 = allocateCPointer(CPointer(CPointer(Byte)), c0)
//    val cc3 = cPointerFrom(c0)

    val ccv = cc0.pointed

    val s0 = allocateCPointer(OSSL_SOMETHING)
    val s1 = allocateCPointer(OSSL_SOMETHING) {
        data_type = 1u
    }
//    val s2 = allocateCPointer(OSSL_SOMETHING, s0.pointed)
//    val s3 = allocateCPointer(s0.pointed)

//    val dtp = s0.data_type
    val dtv = s0.pointed.data_type

//    val o = OSSL_SOMETHING::key

    val r = s0.pointed//.data_type = 0u

    s0.pointed.data_type = 0u

    val sv = s0.pointed

    val newp = sv.copyTo(allocateCPointer(OSSL_SOMETHING))

    val r0 = allocate(OSSL_SOMETHING_TD)
    val r1 = allocate(OSSL_SOMETHING) {
        data_type = 1u
    }

    val sc0 = allocateCPointer(CPointer(OSSL_SOMETHING))
    val sc1 = allocateCPointer(CPointer(OSSL_SOMETHING))
    val sc2 = allocateCPointer(CPointer(OSSL_SOMETHING), s0)
//    val sc3 = cPointerFrom(s0)

    val scv = sc0.pointed

    val st0 = allocateCPointer(OSSL_SOMETHING_TD)
    val st1 = allocateCPointer(OSSL_SOMETHING_TD) { data_type = 1u }
//    val st2 = allocateCPointer(OSSL_SOMETHING_TD, r0) // will copy struct
//    val st3 = allocateCPointer(r0) // will copy struct

    val stv = s0.pointed

    val stc0 = allocateCPointer(CPointer(OSSL_SOMETHING_TD))
    val stc1 = allocateCPointer(CPointer(OSSL_SOMETHING_TD))
    val stc2 = allocateCPointer(CPointer(OSSL_SOMETHING_TD), s0)
//    val stc3 = cPointerFrom(s0)

    val stcv = sc0.pointed

    val o0 = allocateCPointer(SOME_OP)
    val o1 = allocateCPointer(SOME_OP)
//    val o2 = allocateCPointer(SOME_OP, o0.pointed)
//    val o3 = cPointerFrom(o0.pointed)

    val s = allocate(SOME_OP)

//    val ov = o0.pointed

    val oc0 = allocateCPointer(CPointer(SOME_OP))
    val oc1 = allocateCPointer(CPointer(SOME_OP))
    val oc2 = allocateCPointer(CPointer(SOME_OP), o0)
//    val oc3 = cPointerFrom(o0)

    val ocv = oc0.pointed

    val a0 = allocateCArray(CType.Byte, 10)
    val a1 = allocateCArray(Byte, 10)
    val a1i = allocateCArray(CType.Byte, 10) { it.toByte() }
    val a2 = allocateCArray(Byte, ByteArray(10))
    val a3 = allocateCArray(ByteArray(10))

    ByteArray(0).copyInto(a2)

//    val av = a0.pointed
    val avc = a0[0]
    val avcv = avc.pointed
    a0.forEach { ptr ->
    }

    val ac0 = allocateCArray(CPointer(CType.Byte), 10)
    val ac1 = allocateCArray(CPointer(Byte), 10)
    val ac2 = allocateCArray(CPointer(Byte), listOf(b0))
//    val ac3 = allocateArrayFrom(listOf(b0))

//    val acv = ac0.pointed
    val acvc = ac0[0]
    val acvcv = acvc.pointed
    ac0.forEach { ptr ->

    }

    val as0 = allocateCArray(OSSL_SOMETHING, 10)
    val as1 = allocateCArray(OSSL_SOMETHING, 10)
    val as2 = allocateCArray(OSSL_SOMETHING, 10)
    listOf(stv).copyInto(as2)
//    val as3 = allocateArrayFrom(listOf(OSSL_SOMETHING()))

//    val asv = as0.pointed
    as0[0].pointed.apply {
        data_type = 1u
    }

    val asvc = as0[0]
    val asvcv = asvc.pointed
    as0.forEach { ptr ->

    }

    val string = allocateCString("string")
}

private typealias SOME_OP_TD = SOME_OP

internal class SOME_OP private constructor() : COpaque {
    override fun Unsafe.memoryLayout(): MemoryLayout = MemoryLayout.Void()

    companion object : CType<SOME_OP> {
        override fun Unsafe.memoryLayout(): MemoryLayout = MemoryLayout.Void()
    }
}

private typealias OSSL_SOMETHING_TD = OSSL_SOMETHING

private inline val CPointer<OSSL_SOMETHING>.pointed: OSSL_SOMETHING get() = OSSL_SOMETHING(memoryBlock)

// will clash with pointed
//private inline val CPointer<OSSL_SOMETHING>.data_type: CPointer<Byte>
//    get() = TODO()

@JvmInline
private value class OSSL_SOMETHING @PublishedApi internal constructor(
    @PublishedApi internal val block: MemoryBlock
) : CStruct {
    var key: CString?
        get() = block.getPointed(0.toMemorySizeInt(), MemoryLayout.Void)?.let(Unsafe::cString)
        set(value) = block.setPointedAddress(0.toMemorySizeInt(), value?.let(Unsafe::memoryBlock))
    inline var data_type: UInt
        get() = block.getUInt(Companion.data_type_offset.toMemorySizeInt())
        set(value) = block.setUInt(Companion.data_type_offset.toMemorySizeInt(), value)
    var data: CPointer<Unit>?
        get() = TODO()
        set(value) = TODO()
    var data_size: PlatformUInt
        get() = TODO()
        set(value) = TODO()
    var return_size: PlatformUInt
        get() = TODO()
        set(value) = TODO()

    inline val companion: Anonymous.Companion get() = Anonymous.Companion(block)

    override fun Unsafe.memoryLayout(): MemoryLayout = Unsafe.memoryLayout(Companion)

    object Anonymous {
        @JvmInline
        value class Companion @PublishedApi internal constructor(
            private val block: MemoryBlock
        ) : CStruct {
            override fun Unsafe.memoryLayout(): MemoryLayout {
                TODO("Not yet implemented")
            }

            companion object : CType<Anonymous.Companion> {
                override fun Unsafe.memoryLayout(): MemoryLayout {
                    TODO("Not yet implemented")
                }
            }
        }
    }

    companion object : CType<OSSL_SOMETHING> {
        internal const val data_type_offset = 1
        private val memoryLayout = MemoryLayout.of(1, 1)

        override fun Unsafe.memoryLayout(): MemoryLayout = memoryLayout
    }
}
