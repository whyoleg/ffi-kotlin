package dev.whyoleg.foreign.tooling.cx.bridge.aggregator

import dev.whyoleg.foreign.tooling.cx.bridge.model.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*

@Suppress("FunctionName")
public fun CxBridgeFragment(
    fragmentId: CxBridgeFragmentId,
    index: CxCompilerIndex,
    declarationId: CxCompilerDeclaration<*>.() -> CxBridgeDeclarationId
): Pair<CxBridgeFragment, CxBridgeFragmentMapping> {
    fun <T : CxCompilerDeclarationData?> mapIndexDeclarations(declarations: CxCompilerDeclarations<T>) =
        declarations.mapValues { declarationId(it.value) to it.value }.ensureNoCollisions()

    val variablesMapping = mapIndexDeclarations(index.variables)
    val enumsMapping = mapIndexDeclarations(index.enums)
    val recordsMapping = mapIndexDeclarations(index.records)
    val typedefsMapping = mapIndexDeclarations(index.typedefs)
    val functionsMapping = mapIndexDeclarations(index.functions)

    fun CxCompilerDataType.convert(): CxBridgeDataType = when (this) {
        is CxCompilerDataType.Primitive -> CxBridgeDataType.Primitive(value)
        is CxCompilerDataType.Pointer -> CxBridgeDataType.Pointer(pointed.convert())
        is CxCompilerDataType.Enum        -> CxBridgeDataType.Enum(enumsMapping.getValue(id).first)
        is CxCompilerDataType.Typedef     -> CxBridgeDataType.Typedef(typedefsMapping.getValue(id).first)
        is CxCompilerDataType.Record.Reference -> CxBridgeDataType.Record.Reference(recordsMapping.getValue(id).first)
        is CxCompilerDataType.Record.Anonymous -> CxBridgeDataType.Record.Anonymous(
            CxBridgeRecordData(
                isUnion = data.isUnion,
                fields = data.fields.mapIndexed { index, field ->
                    CxBridgeRecordData.Field(
                        name = field.name ?: "p_$index",
                        type = field.type.convert()
                    )
                }
            )
        )
        is CxCompilerDataType.Function -> CxBridgeDataType.Function(
            returnType = returnType.convert(),
            parameters = parameters.map { it.convert() }
        )
        is CxCompilerDataType.Array -> CxBridgeDataType.Array(elementType.convert())
        is CxCompilerDataType.Unsupported -> CxBridgeDataType.Unsupported(info)
    }

    return CxBridgeFragment(
        fragmentId = fragmentId,
        variables = variablesMapping.values.associate { (id, value) ->
            id to CxBridgeVariable(
                id = id,
                returnType = value.data.returnType.convert(),
            )
        },
        enums = enumsMapping.values.associate { (id, value) ->
            id to CxBridgeEnum(
                id = id,
                unnamed = value.data.unnamed,
                constantNames = value.data.constants.map { it.name }.toSet()
            )
        },
        records = recordsMapping.values.associate { (id, value) ->
            id to CxBridgeRecord(
                id = id,
                data = value.data?.let {
                    CxBridgeRecordData(
                        isUnion = it.isUnion,
                        fields = it.fields.mapIndexed { index, field ->
                            CxBridgeRecordData.Field(
                                name = field.name ?: "p_$index",
                                type = field.type.convert()
                            )
                        }
                    )
                }
            )
        },
        typedefs = typedefsMapping.values.associate { (id, value) ->
            id to CxBridgeTypedef(
                id = id,
                aliasedType = value.data.aliasedType.convert(),
                resolvedType = value.data.resolvedType.convert()
            )
        },
        functions = functionsMapping.values.associate { (id, value) ->
            id to CxBridgeFunction(
                id = id,
                isVariadic = value.data.isVariadic,
                returnType = value.data.returnType.convert(),
                parameters = value.data.parameters.mapIndexed { index, parameter ->
                    CxBridgeFunction.Parameter(
                        name = parameter.name ?: "p_$index",
                        aliasNames = emptySet(),
                        type = parameter.type.convert()
                    )
                }
            )
        },
    ) to CxBridgeFragmentMapping(
        fragmentId = fragmentId,
        variables = emptyMap(),
        enums = emptyMap(),
        records = emptyMap(),
        typedefs = emptyMap(),
        functions = emptyMap(),
    )
}

private fun <T : Map<*, Pair<CxBridgeDeclarationId, *>>> T.ensureNoCollisions(): T {
    val collisions =
        values.groupBy { it.first }.filterValues { it.size > 1 }

    check(collisions.isEmpty()) {
        buildString {
            appendLine("There are collisions in naming: ")
            collisions.forEach { (id, values) ->
                appendLine("  ID: $id")
                values.forEach {
                    appendLine("    ${it.second}")
                }
            }
        }
    }
    return this
}
