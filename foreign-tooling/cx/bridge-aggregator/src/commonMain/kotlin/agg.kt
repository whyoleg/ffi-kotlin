package dev.whyoleg.foreign.tooling.cx.bridge.aggregator

import dev.whyoleg.foreign.tooling.cx.bridge.model.*

public fun List<CxBridgeFragment>.shared(): CxBridgeFragment {
    val typedefs = associate {
        // single() to ensure, that we have single such declaration - TODO: may be improve it
        it.id to it.typedefs
    }.run {
        values.intersection { it.keys }.map { id ->
            CxBridgeTypedef(
                id = id,
                aliased = sharedType { it.getValue(id).aliased }
            )
        }
    }

    val records = associate {
        // single() to ensure, that we have single such declaration - TODO: may be improve it
        it.id to it.records
    }.run {
        values.intersection { it.keys }.map { id ->
            val variants = mapValues { it.value.getValue(id) }
            if (
                variants.map { it.value.isUnion }.toSet().size == 1
            ) {
                if (
                    variants.map { it.value.fields == null }.toSet().size == 1
                ) {
                    CxBridgeRecord(
                        id = id,
                        isUnion = variants.values.first().isUnion,
                        fields = when (variants.values.first().fields) {
                            null -> null
                            else -> variants.mapValues {
                                it.value.fields!!.associateBy { it.name }
                            }.run {
                                values.intersection { it.keys }.map { fieldName ->
                                    CxBridgeRecord.Field(
                                        name = fieldName,
                                        type = sharedType { it.getValue(fieldName).type }
                                    )
                                }
                            }
                        }
                    )
                } else error("something bad happens: not all opaque")
            } else error("something bad happens: not all unions|structs")
        }
    }
}

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

private fun <T> Map<CxBridgeFragmentId, T>.sharedType(typeProvider: (T) -> CxBridgeType): CxBridgeType {
    val variants = mapValues { typeProvider(it.value) }
    // TODO - may be don't merge
    return variants.values.distinct().singleOrNull() ?: CxBridgeType.Shared(buildMap {
        variants.forEach { (fragmentId, type) ->
            when (type) {
                is CxBridgeType.Shared -> putAll(type.variants)
                else                   -> put(fragmentId, type)
            }
        }
    })
}
