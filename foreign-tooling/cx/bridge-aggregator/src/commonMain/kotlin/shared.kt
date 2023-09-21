package dev.whyoleg.foreign.tooling.cx.bridge.aggregator

import dev.whyoleg.foreign.tooling.cx.bridge.model.*

public fun CxBridgeFragment(
    fragmentId: CxBridgeFragmentId,
    fragments: Map<CxBridgeFragmentId, CxBridgeFragment>
): CxBridgeFragment = CxBridgeFragment(
    fragmentId = fragmentId,
    typedefs = fragments.intersectionByKeys(CxBridgeFragment::typedefs).associateWith { id ->
        CxBridgeTypedef(
            id = id,
            aliased = fragments.sharedType { it.typedefs.getValue(id).aliased }
        )
    },
    enums = fragments.intersectionByKeys(CxBridgeFragment::enums).associateWith { id ->
        CxBridgeEnum(
            id = id,
            constantNames = fragments.values.intersection { it.enums.getValue(id).constantNames }
        )
    },
    records = fragments.intersectionByKeys(CxBridgeFragment::records).associateWith { id ->
        val variants = fragments.mapValues { it.value.records.getValue(id) }

        val isUnion =
            variants.map { it.value.isUnion }.toSet().singleOrNull()
                ?: error("something bad happens: not all unions|structs")

        val isOpaque =
            variants.map { it.value.fields == null }.toSet().singleOrNull()
                ?: error("something bad happens: not all opaque|not-opaque")

        CxBridgeRecord(
            id = id,
            isUnion = isUnion,
            fields = when {
                isOpaque -> null
                else     -> {
                    val fields = variants.mapValues { it.value.fields!!.associateBy { it.name } }
                    fields.intersectionByKeys { it }.map { fieldName ->
                        CxBridgeRecord.Field(
                            name = fieldName,
                            type = fields.sharedType { it.getValue(fieldName).type }
                        )
                    }
                }
            }
        )
    },
    functions = fragments.intersectionByKeys(CxBridgeFragment::functions).associateWith { id ->
        val variants = fragments.mapValues { it.value.functions.getValue(id) }

        variants.map { it.value.parameters.size }.toSet().singleOrNull()
            ?: error("something bad happens: functions has different amount of arguments")

        CxBridgeFunction(
            id = id,
            returnType = variants.sharedType { it.returnType },
            parameters = run {
                val params = variants.mapValues {
                    it.value.parameters.withIndex().associate { it.index to it.value }
                }
                params.intersectionByKeys { it }.map { parameterIndex ->
                    val names = params.values.map { it.getValue(parameterIndex).name }.toSet()
                    CxBridgeFunction.Parameter(
                        name = names.singleOrNull() ?: "p$parameterIndex",
                        aliasNames = if (names.size == 1) emptySet() else names,
                        type = params.sharedType { it.getValue(parameterIndex).type }
                    )
                }
            }
        )
    },
)

// f1: _uint -> _uint8 -> Long
// f2: _uint -> _uint8 -> Long
// f3: _uint -> Int

// f1+f2: _uint -> _uint8 -> Long
// f1+f2: _uint8 -> Long

// fa: _uint -> Int|Long


// f1: struct.p1 = UInt
// f2: struct.p1 = Int


// f1+f2: struct.p1 = UInt|Int

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
