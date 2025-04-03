package dev.whyoleg.foreign.tool.cbridge

import dev.whyoleg.foreign.tool.cbridge.api.*
import dev.whyoleg.foreign.tool.clang.api.*


// common <- jvmAndNative <- native <- macosArm64
//                        <- jvm
//        <- nonJvm       <- native <- macosArm64
//                        <- js
public sealed class CbridgeFragmentRelation {
    public abstract val fragmentName: String

    public data class Target(
        override val fragmentName: String,
        val targets: Set<CbTarget>,
    ) : CbridgeFragmentRelation() {
        init {
            require(targets.isNotEmpty()) { "targets should be not empty" }
        }
    }

    public data class Shared(
        override val fragmentName: String,
        val dependencies: Set<CbridgeFragmentRelation>,
    ) : CbridgeFragmentRelation() {
        init {
            require(dependencies.isNotEmpty()) { "dependencies should be not empty" }
        }
    }
}

public data class CbridgePackagePattern(
    val packageName: String,
    val patterns: List<Regex>,
)

// N*CxIndex -> M*CbFragment
public fun transform(
    relations: List<CbridgeFragmentRelation>, // roots
    targetIndexes: Map<CbTarget, CxIndex>,
    packagePatterns: List<CbridgePackagePattern>
): List<CbFragment> {
    val targetFragments = targetIndexes.entries.associate { (target, index) ->
        target to convert(target, index, packagePatterns)
    }

    val fragments = mutableMapOf<CbFragmentName, CbFragment>()
    fun visit(relation: CbridgeFragmentRelation) {
        if (relation.fragmentName in fragments) return

        val fragment = when (relation) {
            is CbridgeFragmentRelation.Shared -> {
                relation.dependencies.forEach(::visit)

                commonize(relation.fragmentName, relation.dependencies.map {
                    fragments.getValue(it.fragmentName)
                })
            }

            is CbridgeFragmentRelation.Target -> {
                combine(relation.fragmentName, relation.targets.map {
                    targetFragments.getValue(it)
                })
            }
        }
        fragments.putUnique("Fragment", relation.fragmentName, fragment)
    }
    relations.forEach(::visit)

    return fragments.values.toList()
}

internal fun <K, T> MutableMap<K, T>.putUnique(tag: String, key: K, value: T) {
    require(put(key, value) == null) { "$tag with key: $key already exists" }
}
