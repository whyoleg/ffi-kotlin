package dev.whyoleg.foreign.clang.api

import kotlinx.serialization.*
import kotlinx.serialization.json.*

// TODO: may be add some meta information?
// TODO: decide on builtins - there is only va_list + uint128_t
@Serializable
public data class CxIndex(
    val variables: List<CxVariable>,
    val enums: List<CxEnum>,
    val typedefs: List<CxTypedef>,
    val records: List<CxRecord>,
    val functions: List<CxFunction>
) {

    init {
        ensureAllDeclarationsAccessible()
    }

    public companion object {
        @OptIn(ExperimentalSerializationApi::class)
        private val json = Json {
            prettyPrint = true
            prettyPrintIndent = "  "
        }

        public fun encode(index: CxIndex): String = json.encodeToString(serializer(), index)
        public fun decode(string: String): CxIndex = json.decodeFromString(serializer(), string)
    }

    private fun ensureAllDeclarationsAccessible() {
        val typedefIds = typedefs.mapTo(mutableSetOf()) { it.description.id }
        val recordIds = records.mapTo(mutableSetOf()) { it.description.id }
        val enumIds = enums.mapTo(mutableSetOf()) { it.description.id }

        fun CxType.collectIds() {
            when (this) {
                is CxType.Pointer     -> pointed.collectIds()
                is CxType.Array       -> elementType.collectIds()
//                is CxType.Function    -> {
//                    returnType.collectIds()
//                    parameters.forEach(CxType::collectIds)
//                }

                is CxType.Enum        -> enumIds.add(id)
                is CxType.Record      -> recordIds.add(id)
                is CxType.Typedef     -> typedefIds.add(id)

                CxType.Void,
//                CxType.Bool,
                is CxType.Number,
                is CxType.Unsupported -> {
                }
            }
        }

        fun CxRecordDefinition.collectIds() {
            fields.forEach { it.type.collectIds() }
            anonymousRecords.values.forEach { it.collectIds() }
        }

        variables.forEach {
            it.type.collectIds()
        }
        typedefs.forEach {
            it.aliasedType.collectIds()
            it.resolvedType.collectIds()
        }
        records.forEach {
            it.definition?.collectIds()
        }
        functions.forEach {
            it.returnType.collectIds()
            it.parameters.forEach { parameter -> parameter.type.collectIds() }
        }

        fun stats(
            tag: String,
            collected: Set<CxDeclarationId>,
            actual: Set<CxDeclarationId>
        ) {
            check(
                collected.subtract(actual).isEmpty() &&
                        actual.subtract(collected).isEmpty()
            ) {
                """|$tag missing:
                   |  collected           : ${collected.size}
                   |  actual              : ${actual.size}
                   |  collected x actual  : ${collected.intersect(actual).size}
                   |  actual x collected  : ${actual.intersect(collected).size}
                   |  missing in actual   : ${collected.subtract(actual)}
                   |  missing in collected: ${actual.subtract(collected)}
                """.trimMargin()
            }
        }

        stats("Typedefs", typedefIds, typedefs.mapTo(mutableSetOf()) { it.description.id })
        stats("Records", recordIds, buildSet {
            records.forEach {
                add(it.description.id)
                it.definition?.anonymousRecords?.keys?.let(::addAll)
            }
        })
        stats("Enum", enumIds, enums.mapTo(mutableSetOf()) { it.description.id })
    }
}

// include: openssl/*
public fun CxIndex.filter(
    includedHeaderPatterns: List<Regex> = emptyList(),
    excludedHeaderPatterns: List<Regex> = emptyList(),
    includedVariablePatterns: List<Regex> = emptyList(),
    excludedVariablePatterns: List<Regex> = emptyList(),
    includedEnumPatterns: List<Regex> = emptyList(),
    excludedEnumPatterns: List<Regex> = emptyList(),
    includedTypedefPatterns: List<Regex> = emptyList(),
    excludedTypedefPatterns: List<Regex> = emptyList(),
    includedRecordPatterns: List<Regex> = emptyList(),
    excludedRecordPatterns: List<Regex> = emptyList(),
    includedFunctionPatterns: List<Regex> = emptyList(),
    excludedFunctionPatterns: List<Regex> = emptyList(),
): CxIndex {
    val referencedVariables = mutableSetOf<CxDeclarationId>()
    val referencedEnums = mutableSetOf<CxDeclarationId>()
    val referencedTypedefs = mutableSetOf<CxDeclarationId>()
    val referencedRecords = mutableSetOf<CxDeclarationId>()
    val referencedFunctions = mutableSetOf<CxDeclarationId>()

    fun CxDeclaration.collectReferences() {
        fun CxType.collectReferences() {
            when (this) {
                is CxType.Enum        -> referencedEnums.add(id)
                is CxType.Typedef     -> if (referencedTypedefs.add(id)) {
                    typedefs.first { it.description.id == id }.collectReferences()
                }

                is CxType.Record      -> if (referencedRecords.add(id)) {
                    records.first { it.description.id == id }.collectReferences()
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

        fun CxRecordDefinition.collectReferences() {
            fields.forEach { it.type.collectReferences() }
            // no need to collect anonymousRecords?
        }

        when (this) {
            is CxVariable -> {
                referencedVariables.add(description.id)
                type.collectReferences()
            }

            is CxEnum     -> {
                referencedEnums.add(description.id)
            }

            is CxTypedef  -> {
                referencedTypedefs.add(description.id)
                aliasedType.collectReferences()
                resolvedType.collectReferences()
            }

            is CxRecord   -> {
                referencedRecords.add(description.id)
                definition?.collectReferences()
            }

            is CxFunction -> {
                referencedFunctions.add(description.id)
                returnType.collectReferences()
                parameters.forEach { it.type.collectReferences() }
            }
        }
    }

    fun <D : CxDeclaration> List<D>.collectReferences(
        includedDeclarationPatterns: List<Regex>,
        excludedDeclatationPatterns: List<Regex>,
    ) {
        fun List<Regex>.matches(value: String) = any { it.matches(value) }
        fun CxDeclaration.included(): Boolean = when {
            // exclusion goes first
            excludedHeaderPatterns.matches(description.header) || excludedDeclatationPatterns.matches(description.name) -> false
            includedHeaderPatterns.matches(description.header) || includedDeclarationPatterns.matches(description.name) -> true
            else                                                                                                        -> false
        }
        forEach { declaration ->
            if (declaration.included()) declaration.collectReferences()
        }
    }

    variables.collectReferences(includedVariablePatterns, excludedVariablePatterns)
    // TODO: handle unnamed enums?
    enums.filter { it.description.name.isNotEmpty() }.collectReferences(includedEnumPatterns, excludedEnumPatterns)
    enums.filter { it.description.name.isEmpty() }.collectReferences(emptyList(), emptyList())
    typedefs.collectReferences(includedTypedefPatterns, excludedTypedefPatterns)
    records.collectReferences(includedRecordPatterns, excludedRecordPatterns)
    functions.collectReferences(includedFunctionPatterns, excludedFunctionPatterns)

    return CxIndex(
        variables = variables.filter { it.description.id in referencedVariables },
        enums = enums.filter { it.description.id in referencedEnums },
        typedefs = typedefs.filter { it.description.id in referencedTypedefs },
        records = records.filter { it.description.id in referencedRecords },
        functions = functions.filter { it.description.id in referencedFunctions },
    )
//        .also { new ->
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
