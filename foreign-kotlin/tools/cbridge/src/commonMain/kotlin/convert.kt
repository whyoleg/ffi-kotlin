package dev.whyoleg.foreign.tool.cbridge

import dev.whyoleg.foreign.tool.cbridge.api.*
import dev.whyoleg.foreign.tool.clang.api.*

internal fun convert(
    target: CbTarget,
    index: CxIndex,
    packagePatterns: List<CbridgePackagePattern>
): CbFragment {
    val packages = mutableMapOf<String, CbPackageBuilder>()

    index.declarations.groupBy(CxDeclaration::header).forEach { (header, declarations) ->
        if (header == null) {
            // TODO: what to do with builtins
            declarations.forEach { declaration ->
                println(declaration)
            }
            return@forEach
        }

        val packageName = packageName(header, packagePatterns)
        val packageBuilder = packages.getOrPut(packageName) { CbPackageBuilder(packageName) }
        declarations.forEach(packageBuilder::process)
    }

    return CbFragment(
        name = target, // TODO?
        target = target,
        packages = packages.values.map(CbPackageBuilder::build)
    )
}

private fun packageName(
    header: String,
    packagePatterns: List<CbridgePackagePattern>,
): String {
    packagePatterns.forEach { (packageName, patterns) ->
        if (patterns.any { it.matches(header) }) return packageName
    }
    error("No package name found for header: $header")
}

private class CbPackageBuilder(
    private val name: String
) {
    private val enums = mutableMapOf<String, CbEnum>()
    private val properties = mutableMapOf<String, CbProperty>()

    fun process(declaration: CxDeclaration) {
        when (val data = declaration.data) {
            is CxEnumData -> processEnum(declaration.name, data)
            else          -> {}
//            is CxFunctionData -> TODO()
//            CxOpaqueData      -> TODO()
//            is CxRecordData   -> TODO()
//            is CxTypedefData  -> TODO()
//            is CxVariableData -> TODO()
        }
    }

    fun build(): CbPackage = CbPackage(
        name = name,
        enums = enums.values.toList(),
        properties = properties.values.toList(),
        functions = emptyList(),
        classes = emptyList()
    )

    private fun processEnum(name: String?, data: CxEnumData) {
        when (name) {
            null -> {
                data.constants.forEach { (name, value) ->
                    properties.putUnique(
                        tag = "Property(from Enum)",
                        key = name,
                        value = CbProperty(
                            name = name,
                            isMutable = false,
                            type = CbType.Number(CbNumber.Long) // TODO recheck
                        )
                    )
                }
            }

            else -> {
                val constants = data.constants.map { CbEnumConstant(it.name, it.value) }
                enums.putUnique(
                    tag = "Enum",
                    key = name,
                    value = CbEnum(name, constants)
                )
            }
        }
    }
}
