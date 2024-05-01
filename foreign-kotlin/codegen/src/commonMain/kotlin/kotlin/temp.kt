package dev.whyoleg.foreign.codegen.kotlin

import dev.whyoleg.foreign.bridge.c.*
import dev.whyoleg.foreign.codegen.*

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

internal fun FilesBuilder.kotlinFragmentDirectory(
    configuration: KotlinCodegenConfiguration,
    fragment: CFragment,
    fragmentName: String
) {
    val index = CFragmentIndex(
        fragment.enums.associateBy { it.description.id },
        fragment.typedefs.associateBy { it.description.id },
        fragment.records.associateBy { it.description.id },
    )
//    require(fragment.fragmentType is CFragmentType.Shared)

    declarationGroup(fragment.functions, "functions.$fragmentName") { function ->
        cFunctionDeclaration(configuration, index, KotlinActuality.EXPECT, function).raw("\n")
    }

    declarationGroup(fragment.variables, "variables.$fragmentName") { variable ->
        if (configuration.requiresOptIn != null) {
            annotation(configuration.requiresOptIn)
        }
        variable.description.availableOn?.let { availableOn ->
            annotation(
                "PartialForeignFunctionInterface",
                listOf(availableOn.joinToString(prefix = "[", separator = ", ", postfix = "]") { "\"$it\"" })
            )
        }
        val actuality = KotlinActuality.EXPECT

        // it could be just declaration, not expect in most cases
        listOfNotNull(configuration.visibility.value, actuality.value).forEach { raw("$it ") }
        raw("val ${variable.description.ktName}: ${variable.type.asKotlinTypeString(index)}")
    }

    declarationGroup(fragment.typedefs, "typedefs.$fragmentName") { typedef ->
        if (configuration.requiresOptIn != null) {
            annotation(configuration.requiresOptIn)
        }
        typedef.description.availableOn?.let { availableOn ->
            annotation(
                "PartialForeignFunctionInterface",
                listOf(availableOn.joinToString(prefix = "[", separator = ", ", postfix = "]") { "\"$it\"" })
            )
        }

        val actuality = KotlinActuality.EXPECT

        // it could be just declaration, not expect in most cases
        listOfNotNull(configuration.visibility.value, actuality.value).forEach { raw("$it ") }

        // TODO!!!
        raw("class ${typedef.description.ktName}\n")
    }

    declarationGroup(fragment.enums, "enums.$fragmentName") { enum ->
        val name = enum.description.ktName
        val visibility = configuration.visibility.value
        // TODO: unnamed enums
        annotation("JvmInline")
        raw("$visibility value class $name(override val value: Int): CEnum {\n")
        indented {
            raw("$visibility companion object\n")
        }
        raw("}\n\n")
        enum.constants.forEach {
            raw("$visibility inline val $name.Companion.${it.ktName}: $name get() = $name(${it.value})\n")
        }
    }
    // TODO: may be split records by file?
    declarationGroup(fragment.records, "records.$fragmentName") { record ->
        val name = record.description.ktName
        val visibility = configuration.visibility.value

        when (val definition = record.definition) {
            null -> {
                raw("$visibility expect class $name: COpaque {\n")
                indented {
                    raw("$visibility companion object: CType.Opaque<$name>\n")
                }
                raw("}\n")
            }

            else -> {
                raw("$visibility expect class $name: CStruct {\n")
                indented {
                    raw("$visibility companion object: CType.Record<$name>\n\n")

                    // TODO: support anonymousRecords
                    if (definition.anonymousRecords.isEmpty()) {
                        definition.fields.forEach { field ->
                            raw("$visibility var ${field.ktName}: ${field.type.asKotlinTypeString(index)}\n")
                        }
                    }
                }
                raw("}\n")
            }
        }
    }
}

private fun <D : CDeclaration> FilesBuilder.declarationGroup(
    declarations: List<D>,
    suffix: String,
    block: KotlinCodeBuilder.(declaration: D) -> Unit
) {
    declarations.groupBy {
        it.description.packageName to it.description.headerName
    }.forEach { (key, partialDeclarations) ->
        val (packageName, headerName) = key
        val packagePath = packageName.replace('.', '/')
        val headerPath = headerName//.replace('/', '.')
        kotlinFile(
            fileName = "${packagePath}/${headerPath}.$suffix.kt",
            packageName = packageName,
            imports = listOf(
                "dev.whyoleg.foreign.*",
                "dev.whyoleg.foreign.c.*",
            )
        ) {
            partialDeclarations.forEach {
                block(it)
                raw("\n") // in between declarations
            }
        }
    }
}

internal fun CType.asKotlinTypeString(index: CFragmentIndex): String {
    fun CType.asKotlinTypeString2(index: CFragmentIndex): String = when (this) {
        CType.Boolean        -> "Boolean"
        CType.Void           -> "Unit"
        is CType.Number      -> when (value) {
            CNumber.Byte         -> "Byte"
            CNumber.UByte        -> "UByte"
            CNumber.Short        -> "Short"
            CNumber.UShort       -> "UShort"
            CNumber.Int          -> "Int"
            CNumber.UInt         -> "UInt"
            CNumber.Long         -> "Long"
            CNumber.ULong        -> "ULong"
            CNumber.PlatformInt  -> "PlatformInt"
            CNumber.PlatformUInt -> "PlatformUInt"
            CNumber.Float        -> "Float"
            CNumber.Double       -> "Double"
        }

        is CType.Pointer     -> "CPointer<${pointed.asKotlinTypeString2(index)}>"

        is CType.Array       -> when (size) {
            // TODO: nullability of pointers
            null -> "CArrayPointer<${elementType.asKotlinTypeString2(index)}>"
            else -> "CArray<${elementType.asKotlinTypeString2(index)}> /*size=$size*/"
        }

        is CType.Enum        -> index.enums.getValue(id).description.ktName
        is CType.Record      -> index.records.getValue(id).description.ktName
        is CType.Typedef     -> index.typedefs.getValue(id).description.ktName

        is CType.Function    -> "FUNCTION"
        is CType.Unsupported -> "UNSUPPORTED"
        is CType.Mixed       -> TODO(toString())
    }

    val ktName = asKotlinTypeString2(index)

    // only root type should have a `?`
    return when (resolvedType(index)) {
        is CType.Pointer -> "$ktName?"
        is CType.Array   -> "$ktName?"
        else             -> ktName
    }
}
