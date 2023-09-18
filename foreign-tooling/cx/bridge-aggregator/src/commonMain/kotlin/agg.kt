package dev.whyoleg.foreign.tooling.cx.bridge.aggregator

import dev.whyoleg.foreign.tooling.cx.bridge.model.*

public fun List<CxBridgeFragment>.shared(): CxBridgeFragment {
    val typedefs = map {
        // single() to ensure, that we have single such declaration - TODO: may be improve it
        it.typedefs.groupBy { it.id }.mapValues { it.value.single() }
    }.run {
        intersection { it.keys }.map { id ->
            val variants = map { it.getValue(id).aliased }.distinct()
            CxBridgeTypedef(
                id = id,
                aliased = sharedType { it.getValue(id).aliased }
            )
        }
    }

    val records = map {
        // single() to ensure, that we have single such declaration - TODO: may be improve it
        it.records.groupBy { it.id }.mapValues { it.value.single() }
    }.run {
        intersection { it.keys }.map { id ->
            val variants = map { it.getValue(id) }
            if (
                variants.map { it.isUnion }.toSet().size == 1
            ) {
                if (
                    variants.map { it.fields == null }.toSet().size == 1
                ) {
                    CxBridgeRecord(
                        id = id,
                        isUnion = variants.first().isUnion,
                        fields = when (variants.first().fields) {
                            null -> null
                            else -> variants.map {
                                it.fields!!.associateBy { it.name }
                            }.run {
                                intersection { it.keys }.map { fieldName ->
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

private fun <T, R> List<T>.intersection(selector: (T) -> Set<R>): Set<R> {
    require(isNotEmpty()) { "no values: critical failure" }
    var keys: Set<R>? = null
    forEach {
        val value = selector(it)
        keys = keys?.intersect(value) ?: value
    }
    return keys ?: emptySet()
}

private fun <T> List<T>.sharedType(typeProvider: (T) -> CxBridgeType): CxBridgeType {
    val variants = map(typeProvider).distinct()
    return variants.singleOrNull() ?: CxBridgeType.Shared(variants)
}
