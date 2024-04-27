package dev.whyoleg.foreign.codegen.impl

//private class KotlinJvmFfmFileRenderer(
//    override val context: KotlinRendererContext
//) : KotlinFileGenerator, KotlinJvmFfmRenderer {
//    override fun StringBuilder.appendFile(
//        packageName: String,
//        fileName: String,
//        variables: CDeclarations<CVariableData>,
//        enums: CDeclarations<CEnumData>,
//        typedefs: CDeclarations<CTypedefData>,
//        records: CDeclarations<CRecordData>,
//        functions: CDeclarations<CFunctionData>
//    ) {
//        append("private object $fileName {\n")
//        appendWithIndent {
//            functions.forEach { function ->
//                append("val ${function.id.name} = InternalForeignLinker.methodHandle(")
//                appendWithIndent {
//                    append("name = \"${function.id.name}\",\n")
//                    if (function.data.returnType == CType.Void) {
//                        append("descriptor = FunctionDescriptor.ofVoid(")
//                    } else {
//                        append("descriptor = FunctionDescriptor.of(\n")
//                        appendIndent().append("/* returns */ ${renderLayout(function.data.returnType)},\n")
//                    }
//                    if (function.data.parameters.isNotEmpty()) {
//                        append("\n")
//                        function.data.parameters.forEach { parameter ->
//                            append("/* ${parameter.name} */ ${renderLayout(parameter.type)},\n")
//                        }
//                    }
//                    append(")\n")
//                }
//                append(")\n\n")
//            }
//        }
//        append("}\n")
//    }
//}
//
//private class KotlinJvmFfmFunctionBodyRenderer(
//    override val context: KotlinRendererContext,
//) : KotlinFunctionBodyGenerator {
//    override fun StringBuilder.appendFunctionBody(function: CDeclaration<CFunctionData>) {
//        if (function.data.returnType != CType.Void) {
//            append("return ")
//        }
//        appendForeignFunctionCall(function, "${function.fileName}.${function.id.name}.invokeExact") { type, name ->
//            TODO()
//        }
//        if (function.data.returnType != CType.Void) {
//            append(" as ${renderKotlinType(function.data.returnType)}")
//        }
//    }
//}
//
//private interface KotlinJvmFfmRenderer : KotlinCodeGenerator {
//    fun renderLayout(type: CType): String = TODO()
//}
