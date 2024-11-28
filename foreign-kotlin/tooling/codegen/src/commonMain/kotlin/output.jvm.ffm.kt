package dev.whyoleg.foreign.tooling.codegen

import dev.whyoleg.foreign.tooling.cbridge.*

//@JvmStatic
//val EVP_MD_fetch: MethodHandle = FFI.methodHandle(
//    name = "EVP_MD_fetch",
//    FunctionDescriptor.of(
//        /* resLayout = */ ValueLayout.ADDRESS,
//        /* ...argLayouts = */ ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS
//    )
//)
internal fun KotlinCodeBuilder.jvmFfmMethodHandle(
    function: CFunction
) {
    annotation("JvmStatic")
    raw("private val ${function.description.name}: MethodHandle = JdkForeignLinker.methodHandle(\n")
    indented {
        raw("name = \"${function.description.name}\",\n")
        if (function.returnType.isVoid()) {
            raw("descriptor = FunctionDescriptor.ofVoid(\n")
        } else {
            raw("descriptor = FunctionDescriptor.of(\n")
            indented {
                raw("/* result */ ${function.returnType.toMemoryLayout()},\n")
            }
        }
        raw("/* arguments */\n")
        function.parameters.forEach { parameter ->
            raw("${parameter.type.toMemoryLayout()},\n")
        }
        raw(")\n")
    }
    raw(")\n")
}

private fun CType.toMemoryLayout(): String {
    return when (this) {
        CType.Void           -> TODO()

        is CType.Array       -> "ValueLayout.ADDRESS"
        is CType.Pointer     -> "ValueLayout.ADDRESS"

        is CType.Enum        -> "ValueLayout.JAVA_INT"

        is CType.Number      -> when (value) {
            CNumber.Byte, CNumber.UByte               -> "ValueLayout.JAVA_BYTE"
            CNumber.Short, CNumber.UShort             -> TODO()
            CNumber.Int, CNumber.UInt                 -> TODO()
            CNumber.Long, CNumber.ULong               -> TODO()
            CNumber.PlatformInt, CNumber.PlatformUInt -> TODO()
            CNumber.Float                             -> TODO()
            CNumber.Double                            -> TODO()
        }

        is CType.Mixed       -> TODO()
        is CType.Record      -> TODO()
        is CType.Typedef     -> TODO()
        is CType.Unsupported -> TODO()
    }
}
