package dev.whyoleg.foreign.bridgegen

import dev.whyoleg.foreign.bridge.c.*

public fun mergeFragments(
    fragments: Map<String, CFragment>
) {
    val variableIds = buildSet {
        fragments.forEach {
            it.value.typedefs.forEach {
                add(it.description.id)
            }
        }
    }

    variableIds.forEach { id ->
        val types = fragments.map { (name, fragment) ->
            name to fragment.typedefs.firstOrNull { it.description.id == id }
        }
        if (types.filter { it.second != null }.size > 1) {
            println(id)
            println("ALIASED")
            types.forEach {
                println("  ${it.first}: ${it.second?.aliasedType}")
            }
            println("RESOLVED")
            types.forEach {
                println("  ${it.first}: ${it.second?.resolvedType}")
            }
            println()
        }
    }
}
