package dev.whyoleg.foreign.tooling.cx.bridge.aggregator

import dev.whyoleg.foreign.tooling.cx.bridge.model.*

public fun CxBridgeFragment(
    fragmentId: CxBridgeFragmentId,
    fragments: Map<CxBridgeFragmentId, CxBridgeFragment>
): CxBridgeFragment = CxBridgeFragment(
    fragmentId = fragmentId,
    variables = fragments.intersectionByKeys(CxBridgeFragment::variables).associateWith { id ->
        CxBridgeVariable(
            id = id,
            returnType = fragments.sharedType { it.variables.getValue(id).returnType },
        )
    },
    typedefs = fragments.intersectionByKeys(CxBridgeFragment::typedefs).associateWith { id ->
        CxBridgeTypedef(
            id = id,
            aliasedType = fragments.sharedType { it.typedefs.getValue(id).aliasedType },
            resolvedType = fragments.sharedType { it.typedefs.getValue(id).resolvedType },
        )
    },
    enums = fragments.intersectionByKeys(CxBridgeFragment::enums).associateWith { id ->
        val variants = fragments.mapValues { it.value.enums.getValue(id) }

        val unnamed =
            variants.map { it.value.unnamed }.toSet().singleOrNull()
                ?: error("something bad happens: not all enums unnamed")

        CxBridgeEnum(
            id = id,
            unnamed = unnamed,
            constantNames = fragments.values.intersection { it.enums.getValue(id).constantNames }
        )
    },
    records = fragments.intersectionByKeys(CxBridgeFragment::records).associateWith { id ->
        val variants = fragments.mapValues { it.value.records.getValue(id) }

        val isOpaque =
            variants.map { it.value.data == null }.toSet().singleOrNull()
                ?: error("something bad happens: not all opaque|not-opaque")


        CxBridgeRecord(
            id = id,
            data = when {
                isOpaque -> null
                else     -> {
                    val isUnion =
                        variants.map { it.value.data!!.isUnion }.toSet().singleOrNull()
                            ?: error("something bad happens: not all unions|structs")

                    // TODO is it possible to commonize fields better?
                    val fields = variants.mapValues { it.value.data!!.fields.associateBy { it.name } }
                    CxBridgeRecordData(
                        isUnion = isUnion,
                        fields = fields.intersectionByKeys { it }.map { fieldName ->
                            CxBridgeRecordData.Field(
                                name = fieldName,
                                type = fields.sharedType { it.getValue(fieldName).type }
                            )
                        }
                    )
                }
            }
        )
    },
    functions = fragments.intersectionByKeys(CxBridgeFragment::functions).associateWith { id ->
        val variants = fragments.mapValues { it.value.functions.getValue(id) }

        variants.map { it.value.parameters.size }.toSet().singleOrNull()
            ?: error("something bad happens: functions has different amount of arguments")

        val isVariadic = variants.map { it.value.isVariadic }.toSet().singleOrNull()
            ?: error("something bad happens: not all functions are variadic|non-variadic")

        CxBridgeFunction(
            id = id,
            isVariadic = isVariadic,
            returnType = variants.sharedType { it.returnType },
            parameters = run {
                val params = variants.mapValues {
                    it.value.parameters.withIndex().associate { it.index to it.value }
                }
                params.intersectionByKeys { it }.map { parameterIndex ->
                    val names = params.values.map { it.getValue(parameterIndex).name }.toSet()
                    CxBridgeFunction.Parameter(
                        name = names.singleOrNull() ?: "p_$parameterIndex",
                        aliasNames = if (names.size == 1) emptySet() else names,
                        type = params.sharedType { it.getValue(parameterIndex).type }
                    )
                }
            }
        )
    },
)

private fun <T, R> Collection<T>.intersection(selector: (T) -> Set<R>): Set<R> {
    require(isNotEmpty()) { "no values: critical failure" }
    var keys: Set<R>? = null
    forEach {
        val value = selector(it)
        keys = keys?.intersect(value) ?: value
    }
    return keys ?: emptySet()
}

private fun <T, R> Map<*, T>.intersectionByKeys(selector: (T) -> Map<R, *>): Set<R> {
    require(isNotEmpty()) { "no values: critical failure" }
    var keys: Set<R>? = null
    forEach {
        val value = selector(it.value).keys
        keys = keys?.intersect(value) ?: value
    }
    return keys ?: emptySet()
}

private fun <T> Map<CxBridgeFragmentId, T>.sharedType(typeProvider: (T) -> CxBridgeDataType): CxBridgeDataType {
    val variants = mapValues { typeProvider(it.value) }
    // TODO - may be don't merge
    return variants.values.distinct().singleOrNull() ?: CxBridgeDataType.Shared(buildMap {
        variants.forEach { (fragmentId, type) ->
            when (type) {
                is CxBridgeDataType.Shared -> putAll(type.variants)
                else                       -> put(fragmentId, type)
            }
        }
    })
}
