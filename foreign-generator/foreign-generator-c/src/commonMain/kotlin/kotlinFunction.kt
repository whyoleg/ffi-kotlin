package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.schema.c.*

internal fun CxFunctionInfo.toKotlinExpectDeclaration(
    index: CxIndex,
    libraryName: String,
    visibility: ForeignCDeclaration.Visibility
): String = buildString {
    append(visibility).append(" ")
    append("expect ")
    append(toKotlinDeclaration(index, libraryName, true))
}

internal fun CxFunctionInfo.toKotlinDeclaration(
    index: CxIndex,
    libraryName: String,
    needDefaultScopeValue: Boolean
): String = buildString {
    append("fun ").append(name.value).append("(")
    val parameterDefinitions = buildList {
        parameters.forEach { parameter ->
            add("${parameter.name}: ${parameter.type.type.toKotlinType(index)}")
        }
        if (returnType.type.isRecord(index)) {
            //TODO: name clash
            add(
                when (needDefaultScopeValue) {
                    true  -> "scope: ForeignCScope = ${libraryName}ImplicitScope"
                    false -> "scope: ForeignCScope"
                }
            )
        }
    }
    if (parameterDefinitions.isNotEmpty()) parameterDefinitions.joinTo(
        this,
        prefix = "\n",
        separator = "\n",
        postfix = "\n"
    ) { definition ->
        "$INDENT${definition},"
    }

    append("): ").append(returnType.type.toKotlinType(index))
}
