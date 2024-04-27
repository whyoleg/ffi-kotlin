package dev.whyoleg.foreign.codegen.kotlin

import dev.whyoleg.foreign.bridge.c.*
import dev.whyoleg.foreign.codegen.*
import dev.whyoleg.foreign.codegen.impl.*

// package/`header`.functions.common.kt
// package/`header`.records.common.kt
// package/`header`.typedefs.common.kt
// package/`header`.enums.common.kt
// package/`header`.variables.common.kt

// package/`header`.functions.jvm.jni.kt
// package/`header`.records.jvm.jni.kt
// package/`header`.typedefs.jvm.jni.kt
// package/`header`.enums.jvm.jni.kt
// package/`header`.variables.jvm.jni.kt

// for functions.jvm.jni: generate JNI object with function calls
// for functions.jvm.ffm: generate FFM object with function calls
// for functions.jvm: generate functions + call to JNI/FFM

// for typedefs - generate typealias
//   if aliased types are different - generate expect/actual?

// for enums - generate value class (in common/shared) + top-level-extensions for values
// for variables - generate top-level val/var

// for records - generate expect/actual?

internal fun FilesBuilder.generateKotlin(
    configuration: KotlinCodegenConfiguration,
    fragment: CFragment,
    fragmentName: String
) {
    require(fragment.fragmentType is CFragmentType.Shared)

    fragment.functions.groupBy {
        it.packageName to it.headerName
    }.forEach { (key, functions) ->
        val (packageName, headerName) = key
        val packagePath = packageName.replace('.', '/')
        val headerPath = headerName.replace('/', '_')
        kotlinFile(
            fileName = "${packagePath}/${headerPath}.functions.${fragmentName}.kt",
            packageName = packageName,
            imports = listOf(
                "dev.whyoleg.foreign.*",
                "dev.whyoleg.foreign.c.*",
            )
        ) {
            functions.forEach {
                expectFunction(configuration, it)
                raw("\n") // in between functions
            }
        }
    }

    fragment.functions.groupBy {
        it.packageName to it.headerName
    }.forEach { (key, functions) ->
        val (packageName, headerName) = key
        val packagePath = packageName.replace('.', '/')
        val headerPath = headerName.replace('/', '_')
        kotlinFile(
            fileName = "${packagePath}/${headerPath}.functions.${fragmentName}.kt",
            packageName = packageName,
            imports = listOf(
                "dev.whyoleg.foreign.*",
                "dev.whyoleg.foreign.c.*",
            )
        ) {
            functions.forEach {
                function(configuration, true, it, KotlinCodeBuilder::jsFunctionBody)
                raw("\n") // in between functions
            }
        }
    }
}

private fun KotlinCodeBuilder.function(
    configuration: KotlinCodegenConfiguration,
    isActual: Boolean,
    function: CDeclaration<CFunction>,
    body: KotlinCodeBuilder.(function: CDeclaration<CFunction>) -> Unit
): KotlinCodeBuilder = apply {
    val actuality = when {
        isActual -> KotlinActuality.ACTUAL
        else     -> KotlinActuality.NONE
    }
    functionDeclaration(configuration, actuality, function)
    raw("{\n")
    indented {
        body(function)
    }
    raw("}\n")
}

private fun KotlinCodeBuilder.expectFunction(
    configuration: KotlinCodegenConfiguration,
    function: CDeclaration<CFunction>,
): KotlinCodeBuilder = functionDeclaration(configuration, KotlinActuality.EXPECT, function).raw("\n")

// no new line at the end
private fun KotlinCodeBuilder.functionDeclaration(
    configuration: KotlinCodegenConfiguration,
    actuality: KotlinActuality,
    function: CDeclaration<CFunction>,
): KotlinCodeBuilder = apply {
    if (configuration.requiresOptIn != null) {
        annotation(configuration.requiresOptIn)
    }
    function.availableOn?.let { availableOn ->
        annotation(
            "PartialForeignFunctionInterface",
            listOf(availableOn.joinToString(prefix = "[", separator = ", ", postfix = "]") { "\"$it\"" })
        )
    }

    val visibility = when (configuration.publicApi) {
        true -> KotlinVisibility.PUBLIC
        else -> KotlinVisibility.INTERNAL
    }

    listOfNotNull(visibility.value, actuality.value, "fun").forEach { raw("$it ") }

    if (function.data.returnType.returnsPointer) {
        raw("MemoryScope.${function.name}")
    } else {
        raw(function.name)
    }

    if (function.data.parameters.isEmpty()) {
        raw("()")
    } else {
        raw("(\n")
        indented {
            function.data.parameters.forEach { parameter ->
                raw("${parameter.name}: ${parameter.type.asKotlinTypeString()}, ")
                raw("// C names: ${parameter.cNames.joinToString()}\n")
            }
            if (function.data.returnType.isPointerLike) {
                // TODO: name clash
                raw("cleanup: MemoryCleanupAction<${function.data.returnType.asKotlinTypeString()}>?")
                if (actuality != KotlinActuality.ACTUAL) raw(" = null") // default argument
                raw(",\n")
            }
        }
        raw(")")
    }

    if (!function.data.returnType.isVoid) {
        raw(": ${function.data.returnType.asKotlinTypeString()}")
    }
}

internal fun CType.asKotlinTypeString(): String = TODO()
