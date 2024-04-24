package dev.whyoleg.foreign.codegen

import dev.whyoleg.foreign.bridge.c.*

internal enum class KotlinVisibility(
    val value: String
) {
    PUBLIC("public"),
    INTERNAL("internal"),
    PRIVATE("private")
}

internal enum class KotlinActuality(
    val value: String?
) {
    EXPECT("expect"),
    ACTUAL("actual"),
    NONE(null)
}

internal class KotlinRenderer(
    private val indent: String,
    private val requiresOptIn: String?,
    private val visibility: KotlinVisibility,
) {
    fun StringBuilder.appendKotlinJvmJniFunctionsHolder(
        fileName: String,
        functions: List<CDeclaration<CFunctionData>>
    ) {
        fun renderKotlinJniType(type: CType): String = TODO()

        append("private object $fileName {\n")
        appendWithIndent {
            functions.forEach { function ->
                append("@JvmStatic\n")
                append("external fun ${function.name}")
                if (function.data.parameters.isEmpty()) {
                    append("()")
                } else {
                    append("(\n")
                    appendWithIndent {
                        function.data.parameters.forEach { parameter ->
                            append("${parameter.name}: ${renderKotlinJniType(parameter.type)},\n")
                        }
                    }
                    append(")")
                }
                if (function.data.returnType != CType.Void) {
                    append(": ${renderKotlinJniType(function.data.returnType)}")
                }
                append("\n\n")
            }
        }
        append("}\n")
    }

    fun StringBuilder.appendKotlinJvmFfmFunctionsHolder(
        fileName: String,
        functions: List<CDeclaration<CFunctionData>>
    ) {
        fun renderLayout(type: CType): String = TODO()

        append("private object $fileName {\n")
        appendWithIndent {
            functions.forEach { function ->
                append("val ${function.name} = InternalForeignLinker.methodHandle(")
                appendWithIndent {
                    append("name = \"${function.name}\",\n")
                    if (function.data.returnType == CType.Void) {
                        append("descriptor = FunctionDescriptor.ofVoid(")
                    } else {
                        append("descriptor = FunctionDescriptor.of(\n")
                        append(indent).append("/* returns */ ${renderLayout(function.data.returnType)},\n")
                    }
                    if (function.data.parameters.isNotEmpty()) {
                        append("\n")
                        function.data.parameters.forEach { parameter ->
                            append("/* ${parameter.name} */ ${renderLayout(parameter.type)},\n")
                        }
                    }
                    append(")\n")
                }
                append(")\n\n")
            }
        }
        append("}\n")
    }

    fun StringBuilder.appendKotlinFunction(
        isActual: Boolean,
        function: CDeclaration<CFunctionData>,
        body: StringBuilder.() -> Unit
    ) {
        appendKotlinFunctionDeclaration(if (isActual) KotlinActuality.ACTUAL else KotlinActuality.NONE, function)
        append(" {\n")
        appendWithIndent(body)
        append("\n}\n")
    }

    fun StringBuilder.appendKotlinExpectFunction(function: CDeclaration<CFunctionData>) {
        appendKotlinFunctionDeclaration(KotlinActuality.EXPECT, function)
        append("\n")
    }

    fun StringBuilder.appendKotlinJvmJniFunctionBody(function: CDeclaration<CFunctionData>) {
        if (function.data.returnType != CType.Void) {
            append("return ")
        }
        appendForeignFunctionCall(function, "${function.fileName}.${function.name}") { type, name ->
            TODO()
        }
        // TODO: conversion
    }

    fun StringBuilder.appendKotlinJvmFfmFunctionBody(function: CDeclaration<CFunctionData>) {
        if (function.data.returnType != CType.Void) {
            append("return ")
        }
        appendForeignFunctionCall(function, "${function.fileName}.${function.name}.invokeExact") { type, name ->
            TODO()
        }
        if (function.data.returnType != CType.Void) {
            append(" as ${renderKotlinType(function.data.returnType)}")
        }
    }

    fun StringBuilder.appendKotlinNativeFunctionBody(function: CDeclaration<CFunctionData>) {
        if (function.data.returnType != CType.Void) {
            append("return ")
        }
        appendForeignFunctionCall(function, "ffi_${function.name}") { type, name ->
            TODO()
        }
    }

    fun StringBuilder.appendKotlinJsFunctionBody(function: CDeclaration<CFunctionData>) {
        if (function.data.returnType != CType.Void) {
            append("return ")
        }
        appendForeignFunctionCall(function, "${function.fileName}.${function.name}") { type, name ->
            TODO()
        }
    }

    fun StringBuilder.appendKotlinWasmJsFunctionBody(function: CDeclaration<CFunctionData>) {
        if (function.data.returnType != CType.Void) {
            append("return ")
        }
        appendForeignFunctionCall(function, "ffi_${function.name}") { type, name ->
            TODO()
        }
    }

    // TODO: handle returns struct
    private fun StringBuilder.appendForeignFunctionCall(
        function: CDeclaration<CFunctionData>,
        call: String,
        renderParameter: StringBuilder.(name: String, type: CType) -> Unit
    ) {
        append(call)
        if (function.data.parameters.isEmpty()) {
            append("()")
        } else {
            append("(\n")
            appendWithIndent {
                function.data.parameters.forEach { parameter ->
                    append("${renderParameter(parameter.name, parameter.type)},\n")
                }
            }
            append(")")
        }
    }

    private fun StringBuilder.appendKotlinFunctionDeclaration(
        actuality: KotlinActuality,
        function: CDeclaration<CFunctionData>
    ) {
        if (requiresOptIn != null) {
            append("@$requiresOptIn\n")
        }
        function.availableOn?.joinTo(
            buffer = this,
            prefix = "@PartialForeignFunctionInterface([", separator = ", ", postfix = "])\n"
        ) { "\"$it\"" }

        listOfNotNull(visibility.value, actuality.value, "fun").joinTo(
            buffer = this,
            prefix = "", separator = " ", postfix = " "
        )

        if (function.data.returnType.returnsPointer) {
            append("MemoryScope.${function.name}")
        } else {
            append(function.name)
        }

        if (function.data.parameters.isEmpty()) {
            append("()")
        } else {
            append("(\n")
            appendWithIndent {
                function.data.parameters.forEach { parameter ->
                    append("${parameter.name}: ${renderKotlinType(parameter.type)},")
                    parameter.cNames.joinTo(
                        buffer = this,
                        prefix = " // C names: ", separator = ", ", postfix = "\n"
                    )
                }
                if (function.data.returnType.isPointerLike) {
                    append("cleanup: MemoryCleanupAction<${renderKotlinType(function.data.returnType)}>?")
                    if (actuality == KotlinActuality.ACTUAL) {
                        append(",\n")
                    } else {
                        // default argument
                        append(" = null,\n")
                    }
                }
            }
            append(")")
        }

        if (function.data.returnType != CType.Void) {
            append(": ${renderKotlinType(function.data.returnType)}")
        }
    }

    private fun renderKotlinType(type: CType): String = TODO()

    private fun StringBuilder.appendWithIndent(block: StringBuilder.() -> Unit) {
        append(StringBuilder().apply(block).toString().prependIndent(indent))
    }

    // TODO: better name
    private val CType.returnsPointer: Boolean
        get() = when (this) {
            is CType.Record,
            is CType.Pointer,
            is CType.Array    -> true

            is CType.Typedef  -> TODO()

            is CType.Function -> TODO()
            else              -> false
        }

    private val CType.isPointerLike: Boolean
        get() = when (this) {
            is CType.Pointer,
            is CType.Array    -> true

            is CType.Typedef  -> TODO()
            is CType.Function -> TODO()
            else              -> false
        }
}
