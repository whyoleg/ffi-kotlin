package dev.whyoleg.foreign.tool.clang.api

import kotlinx.serialization.*

// TODO: may be add some meta information?
// TODO: decide on builtins - there is only va_list + uint128_t
// TODO: handle case when record/enum name is null, but there is typedef for it
@Serializable
public data class CxIndex(
    val declarations: Map<CxDeclarationId, CxDeclaration>
) {

    init {
        ensureAllDeclarationsAccessible()
    }

    private fun ensureAllDeclarationsAccessible() {
        val referenced = mutableSetOf<CxDeclarationId>()

        fun CxType.collectIds() {
            when (this) {
                is CxType.Pointer     -> pointed.collectIds()
                is CxType.Array       -> elementType.collectIds()
//                is CxType.Function    -> {
//                    returnType.collectIds()
//                    parameters.forEach(CxType::collectIds)
//                }

                is CxType.Enum        -> referenced.add(id)
                is CxType.Record      -> referenced.add(id)
                is CxType.Typedef     -> referenced.add(id)

                CxType.Void,
//                CxType.Bool,
                is CxType.Number,
                is CxType.Unsupported -> {
                }
            }
        }

        fun CxDeclarationData.collectIds() {
            when (this) {
                is CxVariableData -> type.collectIds()
                is CxTypedefData  -> {
                    aliasedType.collectIds()
                    resolvedType.collectIds()
                }

                is CxRecordData   -> {
                    fields.forEach { field ->
                        field.type.collectIds()
                    }
                    referenced.addAll(anonymousRecords)
                }

                is CxFunctionData -> {
                    returnType.collectIds()
                    parameters.forEach { parameter ->
                        parameter.type.collectIds()
                    }
                }

                CxOpaqueData      -> {}
                is CxEnumData     -> {}
            }
        }

        declarations.values.forEach { it.data.collectIds() }

        check(declarations.keys.containsAll(referenced)) {
            """|missing:
               |  referenced     : ${referenced.size}
               |  declarations   : ${declarations.size}
               |  inaccessible   : ${referenced - declarations.keys}
            """.trimMargin()
        }
    }
}

// include: openssl/*
public fun CxIndex.filter(
    includedHeaderPatterns: List<Regex> = emptyList(),
    excludedHeaderPatterns: List<Regex> = emptyList(),
): CxIndex {
    val referenced = mutableSetOf<CxDeclarationId>()

    fun CxDeclaration.collectReferences() {
        fun CxType.collectReferences() {
            when (this) {
                is CxType.Enum        -> referenced.add(id)
                is CxType.Typedef     -> if (referenced.add(id)) {
                    declarations.getValue(id).collectReferences()
                }

                is CxType.Record      -> if (referenced.add(id)) {
                    declarations.getValue(id).collectReferences()
                }

                is CxType.Pointer     -> pointed.collectReferences()
                is CxType.Array       -> elementType.collectReferences()
//                is CxType.Function    -> {
//                    returnType.collectReferences()
//                    parameters.forEach { it.collectReferences() }
//                }

                CxType.Void,
//                CxType.Bool,
                is CxType.Number,
                is CxType.Unsupported -> {
                }
            }
        }

        when (data) {
            is CxVariableData -> {
                referenced.add(id)
                data.type.collectReferences()
            }

            is CxEnumData     -> {
                referenced.add(id)
            }

            is CxTypedefData  -> {
                referenced.add(id)
                data.aliasedType.collectReferences()
                data.resolvedType.collectReferences()
            }

            is CxRecordData   -> {
                referenced.add(id)
                data.fields.forEach { it.type.collectReferences() }
            }

            is CxFunctionData -> {
                referenced.add(id)
                data.returnType.collectReferences()
                data.parameters.forEach { it.type.collectReferences() }
            }

            CxOpaqueData      -> {}
        }
    }

    fun List<Regex>.matches(value: String?) = if (value == null) false else any { it.matches(value) }
    fun CxDeclaration.included(): Boolean = when {
        // exclusion goes first
        excludedHeaderPatterns.matches(header) -> false
        includedHeaderPatterns.matches(header) -> true
        else                                   -> false
    }

    declarations.values.forEach { declaration ->
        if (declaration.included()) declaration.collectReferences()
    }

    return CxIndex(declarations.filterKeys { it in referenced })
}

// TODO: decide on unsupported filtering
//public fun CxIndex.filterUnsupported(): CxIndex {
//    fun CxDeclaration.isSupported(): Boolean {
//        val visited = mutableSetOf<CxDeclarationId>()
//        var supported = true
//
//        fun CxDeclaration.collectUnsupported() {
//            if (!supported) return
//
//            fun CxType.collectUnsupported() {
//                if (!supported) return
//
//                when (this) {
//                    CxType.Void,
//                    is CxType.Enum,
//                    is CxType.Number      -> {
//                    }
//
//                    is CxType.Array       -> elementType.collectUnsupported()
//                    is CxType.Pointer     -> pointed.collectUnsupported()
//
//                    is CxType.Record      -> if (visited.add(id)) {
//                        // anonymous records handled separately
//                        records.firstOrNull { it.description.id == id }?.collectUnsupported()
//                    }
//
//                    is CxType.Typedef     -> if (visited.add(id)) {
//                        typedefs.first { it.description.id == id }.collectUnsupported()
//                    }
//
//                    is CxType.Unsupported -> supported = false
//                }
//            }
//
//            fun CxRecordDefinition.collectUnsupported() {
//                if (!supported) return
//
//                fields.forEach { it.type.collectUnsupported() }
//                anonymousRecords.values.forEach { it.collectUnsupported() }
//            }
//
//            when (this) {
//                is CxEnum     -> {}
//                is CxVariable -> type.collectUnsupported()
//                is CxFunction -> {
//                    returnType.collectUnsupported()
//                    parameters.forEach { it.type.collectUnsupported() }
//                }
//
//                is CxRecord   -> when (definition) {
//                    null -> {}
//                    else -> definition.collectUnsupported()
//                }
//
//                is CxTypedef  -> {
//                    aliasedType.collectUnsupported()
//                    resolvedType.collectUnsupported()
//                }
//            }
//        }
//
//        collectUnsupported()
//        return supported.also {
//            if(!it) println(this)
//        }
//    }
//
//    return CxIndex(
//        variables = variables.filter { it.isSupported() },
//        enums = enums, // all enums are supported
//        typedefs = typedefs.filter { it.isSupported() },
//        records = records.filter { it.isSupported() },
//        functions = functions.filter { it.isSupported() },
//    ).also { new ->
//        println(
//            """
//            Filtered:
//            * variables ${variables.size} -> ${new.variables.size}
//            * enums ${enums.size} -> ${new.enums.size}
//            * typedefs ${typedefs.size} -> ${new.typedefs.size}
//            * records ${records.size} -> ${new.records.size}
//            * functions ${functions.size} -> ${new.functions.size}
//            """.trimIndent()
//        )
//    }
//}
