package dev.whyoleg.foreign.codegen.kotlin

import dev.whyoleg.foreign.bridge.c.*
import dev.whyoleg.foreign.codegen.*

internal fun KotlinCodeBuilder.cFunction(
    configuration: KotlinCodegenConfiguration,
    index: CFragmentIndex,
    isActual: Boolean,
    function: CFunction,
    body: KotlinCodeBuilder.(function: CFunction) -> Unit
): KotlinCodeBuilder = apply {
    val actuality = when {
        isActual -> KotlinActuality.ACTUAL
        else     -> KotlinActuality.NONE
    }
    cFunctionDeclaration(configuration, index, actuality, function)
    raw("{\n")
    indented {
        body(function)
    }
    raw("}\n")
}

// no new line at the end
internal fun KotlinCodeBuilder.cFunctionDeclaration(
    configuration: KotlinCodegenConfiguration,
    index: CFragmentIndex,
    actuality: KotlinActuality,
    function: CFunction,
): KotlinCodeBuilder = apply {
    if (configuration.requiresOptIn != null) {
        annotation(configuration.requiresOptIn)
    }
    function.description.availableOn?.let { availableOn ->
        annotation(
            "PartialForeignFunctionInterface",
            listOf(availableOn.joinToString(prefix = "[", separator = ", ", postfix = "]") { "\"$it\"" })
        )
    }

    listOfNotNull(configuration.visibility.value, actuality.value).forEach { raw("$it ") }

    if (function.returnType.requiresPointerAllocation(index)) {
        raw("fun MemoryScope.${function.description.ktName}")
    } else {
        raw("fun ${function.description.ktName}")
    }

    if (function.parameters.isNotEmpty() || function.returnType.isPointerLike(index)) {
        raw("(\n")
        indented {
            function.parameters.forEach { parameter ->
                raw("${parameter.ktName}: ${parameter.type.asKotlinTypeString(index)},")
                if (parameter.cNames.isNotEmpty()) raw(" // C names: ${parameter.cNames.joinToString()}")
                raw("\n")
            }
            if (function.returnType.isPointerLike(index)) {
                // TODO: name clash
                raw("cleanup: MemoryCleanupAction<${function.returnType.asKotlinTypeString(index)}>?")
                if (actuality != KotlinActuality.ACTUAL) raw(" = null") // default argument
                raw(",\n")
            }
        }
        raw(")")
    } else {
        raw("()")
    }

    if (!function.returnType.isVoid()) {
        raw(": ${function.returnType.asKotlinTypeString(index)}")
    }
}
