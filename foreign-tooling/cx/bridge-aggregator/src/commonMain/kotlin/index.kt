package dev.whyoleg.foreign.tooling.cx.bridge.aggregator

import dev.whyoleg.foreign.tooling.cx.bridge.model.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*

public fun CxBridgeFragment(
    fragmentId: CxBridgeFragmentId,
    index: CxCompilerIndex,
    declarationId: CxCompilerDeclaration.() -> CxBridgeDeclarationId
): CxBridgeFragment {
    fun CxCompilerDataType.convert(): CxBridgeDataType = when (this) {
        is CxCompilerDataType.Primitive -> CxBridgeDataType.Primitive(value)
        is CxCompilerDataType.Pointer -> CxBridgeDataType.Pointer(pointed.convert())
        is CxCompilerDataType.Enum -> CxBridgeDataType.Enum(declarationId(index.enums.getValue(id)))
        is CxCompilerDataType.Typedef -> CxBridgeDataType.Typedef(declarationId(index.typedefs.getValue(id)))
        is CxCompilerDataType.Record -> CxBridgeDataType.Record(declarationId(index.records.getValue(id)))
        is CxCompilerDataType.Function -> CxBridgeDataType.Function(
            returnType = returnType.convert(),
            parameters = parameters.map { it.convert() }
        )
        is CxCompilerDataType.Array -> CxBridgeDataType.Array(elementType.convert())
        is CxCompilerDataType.Unsupported -> CxBridgeDataType.Unsupported(name, kind)
    }

    // TODO: check, that there is no declarations with the same CxBridgeDeclarationId!!!
    return CxBridgeFragment(
        fragmentId = fragmentId,
        typedefs = index.typedefs.map { (_, value) ->
            CxBridgeTypedef(
                id = declarationId(value),
                aliased = value.aliased.convert()
            )
        }.groupBy { it.id }.mapValues { it.value.single() },
        enums = index.enums.map { (_, value) ->
            CxBridgeEnum(
                id = declarationId(value),
                constantNames = value.constants.map { it.name }.toSet()
            )
        }.groupBy { it.id }.mapValues {
            if (it.value.size > 1) {
                it.value.forEach { println(it) }
            }
            it.value.single()
        },
        records = index.records.map { (_, value) ->
            CxBridgeRecord(
                id = declarationId(value),
                isUnion = value.isUnion,
                fields = value.members?.fields?.map {
                    CxBridgeRecord.Field(it.name, it.type.convert())
                }
            )
        }.groupBy { it.id }.mapValues { it.value.single() },
        functions = index.functions.map { (_, value) ->
            CxBridgeFunction(
                id = declarationId(value),
                returnType = value.returnType.convert(),
                parameters = value.parameters.map {
                    CxBridgeFunction.Parameter(it.name, emptySet(), it.type.convert())
                }
            )
        }.groupBy { it.id }.mapValues { it.value.single() },
    )
}
