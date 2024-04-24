package dev.whyoleg.foreign.c

// for compiler plugin - it's unsafe - very unsafe

//@Target(AnnotationTarget.CLASS)
//public annotation class UnsafeCStruct(val cName: String = "")
//
////// for class only?
////@Target(AnnotationTarget.CLASS)
////public annotation class UnsafeCOpaque(val cName: String = "")
//
////// TODO: it's just typealias to some type - is it needed?
////@Target(AnnotationTarget.CLASS)
////public annotation class UnsafeCTypedef(val cName: String = "")
////
////// TODO: is it needed - it's just a value class with values
////@Target(AnnotationTarget.CLASS)
////public annotation class UnsafeCEnum(val cName: String = "")
//
//@Target(AnnotationTarget.FUNCTION)
//public annotation class UnsafeCFunction(val cName: String = "")
//
//@Target(AnnotationTarget.PROPERTY)
//public annotation class UnsafeCVariable(val cName: String = "")
//
//
//// temp
//
//@UnsafeCVariable
//internal expect val OPENSSL_VERSION_STRING: Int
//
//@UnsafeCFunction // TODO: force MemoryScope(decide), cleanup action added automatically
//internal expect fun MemoryScope.OpenSSL_version(type: Int): CString?
//
//@UnsafeCFunction
//internal expect fun OPENSSL_version_major(): UInt
//
////@UnsafeCOpaque
//internal class OSSL_LIB_CTX
//
////@UnsafeCOpaque
//internal class EVP_MD
//
//@UnsafeCFunction
//internal expect fun MemoryScope.EVP_MD_fetch(
//    ctx: CPointer<OSSL_LIB_CTX>?,
//    algorithm: CString?,
//    properties: CString?,
//): CPointer<EVP_MD>?
//
////@UnsafeCOpaque
//internal expect class OSSL_PARAM {
//    var key: CString?
//    var data_type: UInt
//    var data: CPointer<Unit>?
//    var data_size: PlatformUInt
//    var return_size: PlatformUInt
//}
//
//// struct asn1_type_st {
////    int type;
////    union {
////        char *ptr;
////        int boolean;
////    } value;
////};
//@UnsafeCStruct
//internal expect class NestedStruct {
//    var type: Int
//    var value: Value
//
//    @UnsafeCUnion
//    class Value {
//        var ptr: CPointer<Byte>
//        var boolean: Int
//    }
//}
//
//@UnsafeCFunction
//internal expect fun MemoryScope.OSSL_PARAM_construct_utf8_string(
//    key: CString?,
//    buf: CString?,
//    bsize: PlatformUInt,
//): OSSL_PARAM
//
//@UnsafeCFunction
//internal expect fun MemoryScope.OSSL_PARAM_construct_end(): OSSL_PARAM
//
//// examples
//
////@UnsafeCEnum
////public expect class SOME_VALUE {
////    public companion object {
////        public val V1: SOME_VALUE
////    }
////}
//
////public inline val SOME_VALUE.Companion.V1: SOME_VALUE get() = SOME_VALUE(1)
////public inline val SOME_VALUE.Companion.V2: SOME_VALUE get() = SOME_VALUE(5)
//
//public object SOME_OP : COpaque, CType<SOME_OP>
//
//public inline fun CType.Companion.SOME_OP(): CType<SOME_OP> = TODO()
//
//@JvmInline
//public value class OSSL_SOMETHING internal constructor(
//    private val block: MemoryBlock
//) : CStruct<OSSL_SOMETHING> {
//    public var s: Int
//        get() = 1
//        set(value) {
//
//        }
//
//    public companion object : CType<OSSL_SOMETHING> {
//        private val field = lazy { }
//    }
//}
//
//public inline fun CType.Companion.OSSL_SOMETHING(): CType<OSSL_SOMETHING> = TODO()
//
//public inline fun MemoryScope.OSSL_SOMETHING(block: OSSL_SOMETHING.() -> Unit = {}): OSSL_SOMETHING = TODO()
