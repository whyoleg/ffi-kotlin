package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*

internal fun CxFunctionInfo.toCJniDeclaration(
    index: CxIndex,
    kotlinPackage: String,
    kotlinClassName: String,
): String = buildString {
    append("JNIEXPORT ").append(returnType.type.toCJniType(index)).append(" JNICALL Java_")
    append(kotlinPackage.replace(".", "_")).append("_")
    append(kotlinClassName).append("_")
    append(prefixedName.replace("_", "_1"))
    append("(JNIEnv* env, jclass jclss")

    parametersWithReturnType(index).joinToIfNotEmpty(
        this,
        prefix = ",\n",
        separator = ",\n",
        postfix = "\n"
    ) { parameter ->
        "$INDENT${parameter.type.type.toCJniType(index)} p_${parameter.name}"
    }
    append(") {\n")
    append(INDENT)
        .append(returnBlock(index, returnType.name, returnType.type.toCJniType(index)))
        .append(name.value).append("(")

    parameters.joinToIfNotEmpty(
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

private fun CxType.toCJniType(index: CxIndex): String = when (this) {
    CxType.Char               -> "jbyte"
    CxType.Byte               -> "jbyte"
    CxType.UByte              -> "jbyte"
    CxType.Short              -> "jshort"
    CxType.UShort             -> "jshort"
    CxType.Int                -> "jint"
    CxType.UInt               -> "jint"
    CxType.Long               -> "jlong"
    CxType.ULong              -> "jlong"
    CxType.LongLong           -> "jlong"
    CxType.ULongLong          -> "jlong"
    CxType.Float              -> "jfloat"
    CxType.Double             -> "jdouble"
    CxType.Void               -> "void"

    is CxType.Typedef         -> index.typedef(id).aliased.type.toCJniType(index)
    is CxType.Record          -> "void" // we return it via a pointer
    is CxType.Enum            -> "jint"
    is CxType.ConstArray      -> "jlong"
    is CxType.IncompleteArray -> "jlong"
    is CxType.Pointer         -> "jlong"
    else                      -> TODO(toString())
}
