package dev.whyoleg.foreign.codegen.impl

//private class KotlinJvmJniFileRenderer(
//    override val context: KotlinRendererContext
//) : KotlinFileGenerator, KotlinJvmJniRenderer {
//
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
//                append("@JvmStatic\n")
//                append("external fun ${function.id.name}")
//                if (function.data.parameters.isEmpty()) {
//                    append("()")
//                } else {
//                    append("(\n")
//                    appendWithIndent {
//                        function.data.parameters.forEach { parameter ->
//                            append("${parameter.name}: ${renderKotlinJniType(parameter.type)},\n")
//                        }
//                    }
//                    append(")")
//                }
//                if (function.data.returnType != CType.Void) {
//                    append(": ${renderKotlinJniType(function.data.returnType)}")
//                }
//                append("\n\n")
//            }
//        }
//        append("}\n")
//    }
//}
//
//private object KotlinJvmJniFunctionBodyRenderer : KotlinFunctionBodyRenderer {
//    override fun KotlinFunctionBodyGenerationContext.renderFunctionBody(
//        function: CDeclaration<CFunctionData>
//    ): String = buildString {
//        if (function.data.returnType != CType.Void) {
//            append("return ")
//        }
//        appendForeignFunctionCall(function, "${function.fileName}.${function.id.name}") { type, name ->
//            TODO()
//        }
//        // TODO: conversion
//    }
//}
//
//private fun renderKotlinJniType(type: CType): String = TODO()
