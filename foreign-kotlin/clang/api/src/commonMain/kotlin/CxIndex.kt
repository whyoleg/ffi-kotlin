package dev.whyoleg.foreign.clang.api

import kotlinx.serialization.*
import kotlinx.serialization.json.*

// TODO: may be add some meta information?
// TODO: decide on builtins - there is only va_list + uint128_t
@Serializable
public data class CxIndex(
    val variables: CxDeclarations<CxVariableData>,
    val enums: CxDeclarations<CxEnumData>,
    val typedefs: CxDeclarations<CxTypedefData>,
    val records: CxDeclarations<CxRecordData>,
    val functions: CxDeclarations<CxFunctionData>
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
        val typedefIds = typedefs.mapTo(mutableSetOf(), CxDeclaration<*>::id)
        val recordIds = records.mapTo(mutableSetOf(), CxDeclaration<*>::id)
        val enumIds = enums.mapTo(mutableSetOf(), CxDeclaration<*>::id)

        fun CxType.collectIds() {
            when (this) {
                is CxType.Pointer     -> pointed.collectIds()
                is CxType.Array       -> elementType.collectIds()
                is CxType.Function    -> {
                    returnType.collectIds()
                    parameters.forEach(CxType::collectIds)
                }

                is CxType.Enum        -> enumIds.add(id)
                is CxType.Record      -> recordIds.add(id)
                is CxType.Typedef     -> typedefIds.add(id)

                CxType.Void,
                CxType.Bool,
                is CxType.Number,
                is CxType.Unsupported -> {
                }
            }
        }

        fun CxRecordDefinition.collectIds() {
            fields.forEach { it.fieldType.collectIds() }
        }

        variables.forEach {
            it.data.variableType.collectIds()
        }
        typedefs.forEach {
            it.data.aliasedType.collectIds()
            it.data.resolvedType.collectIds()
        }
        records.forEach {
            when (val data = it.data) {
                is CxAnonymousRecordData -> data.definition.collectIds()
                is CxBasicRecordData     -> data.definition.collectIds()
                is CxOpaqueRecordData    -> {}
            }
        }
        functions.forEach {
            it.data.returnType.collectIds()
            it.data.parameters.forEach { it.type.collectIds() }
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

        stats("Typedefs", typedefIds, typedefs.mapTo(mutableSetOf(), CxDeclaration<*>::id))
        stats("Records", recordIds, records.mapTo(mutableSetOf(), CxDeclaration<*>::id))
        stats("Enum", enumIds, enums.mapTo(mutableSetOf(), CxDeclaration<*>::id))
    }
}

// include: openssl/*
public fun CxIndex.filter(
    includedHeaderPatterns: List<Regex>,
    excludedHeaderPatterns: List<Regex>
): CxIndex {
    val matches = mutableMapOf<String, Boolean>()
    fun matches(header: CxDeclarationHeader): Boolean = matches.getOrPut(header) {
        if (excludedHeaderPatterns.any { it.matches(header) }) false
        else if (includedHeaderPatterns.any { it.matches(header) }) true
        else false
    }

    val referencedVariables = mutableSetOf<CxDeclarationId>()
    val referencedEnums = mutableSetOf<CxDeclarationId>()
    val referencedTypedefs = mutableSetOf<CxDeclarationId>()
    val referencedRecords = mutableSetOf<CxDeclarationId>()
    val referencedFunctions = mutableSetOf<CxDeclarationId>()

    fun CxDeclaration<*>.collectReferences() {
        fun CxType.collectReferences() {
            when (this) {
                is CxType.Enum        -> referencedEnums.add(id)
                is CxType.Typedef     -> if (referencedTypedefs.add(id)) {
                    typedefs.find { it.id == id }?.collectReferences()
                }

                is CxType.Record      -> if (referencedRecords.add(id)) {
                    records.find { it.id == id }?.collectReferences()
                }

                is CxType.Pointer     -> pointed.collectReferences()
                is CxType.Array       -> elementType.collectReferences()
                is CxType.Function    -> {
                    returnType.collectReferences()
                    parameters.forEach { it.collectReferences() }
                }

                CxType.Void,
                CxType.Bool,
                is CxType.Number,
                is CxType.Unsupported -> {
                }
            }
        }

        when (data) {
            is CxVariableData -> {
                referencedVariables.add(id)
                data.variableType.collectReferences()
            }

            is CxEnumData     -> {
                referencedEnums.add(id)
            }

            is CxTypedefData  -> {
                referencedTypedefs.add(id)
                data.aliasedType.collectReferences()
                data.resolvedType.collectReferences()
            }

            is CxRecordData   -> {
                referencedRecords.add(id)
                when (data) {
                    is CxAnonymousRecordData -> data.definition
                    is CxBasicRecordData     -> data.definition
                    is CxOpaqueRecordData    -> null
                }?.fields?.forEach { it.fieldType.collectReferences() }
            }

            is CxFunctionData -> {
                referencedFunctions.add(id)
                data.returnType.collectReferences()
                data.parameters.forEach { it.type.collectReferences() }
            }
        }
    }

    fun <D : CxDeclarationData> CxDeclarations<D>.collectReferences() {
        forEach { declaration ->
            if (matches(declaration.header)) declaration.collectReferences()
        }
    }

    variables.collectReferences()
    enums.collectReferences()
    typedefs.collectReferences()
    records.collectReferences()
    functions.collectReferences()

    return CxIndex(
        variables = variables.filter { it.id in referencedVariables },
        enums = enums.filter { it.id in referencedEnums },
        typedefs = typedefs.filter { it.id in referencedTypedefs },
        records = records.filter { it.id in referencedRecords },
        functions = functions.filter { it.id in referencedFunctions },
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
