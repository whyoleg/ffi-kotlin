package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*

//TODO: struct support
internal fun CxFunctionInfo.toCJniDeclaration(
    kotlinPackage: String,
    kotlinClassName: String,
): String = buildString {
    append("JNIEXPORT ").append(returnType.type.toCJniType()).append(" JNICALL Java_")
    append(kotlinPackage.replace(".", "_")).append("_")
    append(kotlinClassName).append("_")
    append(prefixedName.replace("_", "_1"))
    append("(JNIEnv* env, jclass jclss")
    if (parameters.isNotEmpty()) parameters.joinTo(
        this,
        prefix = ",\n",
        separator = ",\n",
        postfix = "\n"
    ) { parameter ->
        "$INDENT${parameter.type.type.toCJniType()} p_${parameter.name}"
    }
    append(") {\n")
    append(INDENT).append("return (").append(returnType.type.toCJniType()).append(") ").append(name.value).append("(")

    if (parameters.isNotEmpty()) parameters.joinTo(
        this,
        prefix = "\n",
        separator = ",\n",
        postfix = "\n$INDENT"
    ) { parameter ->
        "$INDENT$INDENT(${parameter.type.name}) p_${parameter.name}"
    }
    append(");")
    append("\n}")
}

//JNIEXPORT jlong JNICALL Java_dev_whyoleg_ffi_libcrypto3_err_ERR_1error_1string (JNIEnv* env, jclass jclss,
//jlong p_e,
//jlong p_buf
//) {
//    return (jlong)ERR_error_string(p_e, (char*)p_buf);
//}


private fun CxType.toCJniType(): String = when (this) {
//    is CxType.ConstArray      -> TODO()
//    is CxType.IncompleteArray -> TODO()
//    CxType.Boolean            -> TODO()
//    CxType.Byte               -> TODO()
//    CxType.Char               -> TODO()
//    CxType.Double             -> TODO()
//    is CxType.Enum            -> TODO()
//    CxType.Float              -> TODO()
//    is CxType.Function        -> TODO()
//    CxType.Int                -> TODO()
//    CxType.Int128             -> TODO()
//    CxType.Long               -> TODO()
//    CxType.LongDouble         -> TODO()
//    CxType.LongLong           -> TODO()
    is CxType.Pointer -> "jlong"
//    CxType.Short              -> TODO()
//    is CxType.Struct          -> TODO()
//    is CxType.Typedef         -> TODO()
//    CxType.UByte              -> TODO()
//    CxType.UInt               -> TODO()
//    CxType.UInt128            -> TODO()
    CxType.ULong      -> "jlong"
//    CxType.ULongLong          -> TODO()
//    CxType.UShort             -> TODO()
//    is CxType.Unknown         -> TODO()
    CxType.Void       -> "void"
    else              -> TODO(toString())
}
