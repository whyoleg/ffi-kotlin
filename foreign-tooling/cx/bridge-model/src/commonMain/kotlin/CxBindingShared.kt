package dev.whyoleg.foreign.tooling.cx.model

@Serializable
public data class CxBindingSharedModule(
    val packages: Map<CxBindingPackageName, CxBindingsSharedPackage>
)

@Serializable
public data class CxBindingsSharedPackage(
//    val typedefs: Map<CxDeclarationName, CxBindingSharedTypedef>,
    val enums: Map<CxDeclarationName, CxBindingSharedEnum>,
    val records: Map<CxDeclarationName, CxBindingSharedRecord>
)

@Serializable
public data class CxBindingSharedTypedef(
    val constantNames: Set<String>,
)

@Serializable
public data class CxBindingSharedEnum(
    val constantNames: Set<String>,
)

@Serializable
public data class CxBindingSharedRecord(
    val fieldNames: Set<String>
)

/*internal*/ public fun List<CxBindingSharedModule>.combine(): CxBindingSharedModule {
    require(isNotEmpty()) { "no modules: critical failure" }
    if (size == 1) return this[0]

    return CxBindingSharedModule(
        packages = intersection { it.packages.keys }.associateWith { packageName ->
            val pkgs = map { it.packages.getValue(packageName) }

            CxBindingsSharedPackage(
                enums = pkgs.intersection { it.enums.keys }.associateWith { name ->
                    val enums = pkgs.map { it.enums.getValue(name) }

                    CxBindingSharedEnum(
                        constantNames = enums.intersection { it.constantNames }
                    )
                },
                records = pkgs.intersection { it.records.keys }.associateWith { name ->
                    val records = pkgs.map { it.records.getValue(name) }

                    CxBindingSharedRecord(
                        fieldNames = records.intersection { it.fieldNames }
                    )
                }
            )
        }
    )
}

private fun <T, R> List<T>.intersection(selector: (T) -> Set<R>): Set<R> {
    require(isNotEmpty()) { "no values: critical failure" }
    var keys: Set<R>? = null
    forEach {
        val value = selector(it)
        keys = keys?.intersect(value) ?: value
    }
    return keys ?: emptySet()
}
