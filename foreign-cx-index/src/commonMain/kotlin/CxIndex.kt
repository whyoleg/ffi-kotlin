package dev.whyoleg.foreign.cx.index

import kotlinx.serialization.*

@Serializable
public data class CxIndex(
    val builtIn: CxHeaderInfo,
    val headers: List<CxHeaderInfo> = emptyList()
) {
    private val declarations: Declarations by lazy {
        val typedefs = mutableMapOf<CxDeclarationId, CxTypedefInfo>()
        val structs = mutableMapOf<CxDeclarationId, CxStructInfo>()
        val enums = mutableMapOf<CxDeclarationId, CxEnumInfo>()
        val functions = mutableMapOf<CxDeclarationId, CxFunctionInfo>()
        (headers + builtIn).forEach { header ->
            header.typedefs.forEach { typedefs[it.id] = it }
            header.structs.forEach { structs[it.id] = it }
            header.enums.forEach { enums[it.id] = it }
            header.functions.forEach { functions[it.id] = it }
        }
        Declarations(typedefs, structs, enums, functions)
    }

    public fun typedef(id: CxDeclarationId): CxTypedefInfo = declarations.typedefs.getValue(id)
    public fun struct(id: CxDeclarationId): CxStructInfo = declarations.structs.getValue(id)
    public fun enum(id: CxDeclarationId): CxEnumInfo = declarations.enums.getValue(id)
    public fun function(id: CxDeclarationId): CxFunctionInfo = declarations.functions.getValue(id)

    public fun filter(block: Filter.() -> Unit): CxIndex = Filter().apply(block).applyTo(this)

    public class Filter internal constructor() {
        private val headerFilters = mutableListOf<(CxHeaderInfo) -> Boolean>()
        private val typedefFilters = mutableListOf<(CxTypedefInfo) -> Boolean>()
        private val structFilters = mutableListOf<(CxStructInfo) -> Boolean>()
        private val enumFilters = mutableListOf<(CxEnumInfo) -> Boolean>()
        private val functionFilters = mutableListOf<(CxFunctionInfo) -> Boolean>()

        public fun headers(block: (info: CxHeaderInfo) -> Boolean) {
            headerFilters += block
        }

        public fun typedefs(block: (info: CxTypedefInfo) -> Boolean) {
            typedefFilters += block
        }

        public fun structs(block: (info: CxStructInfo) -> Boolean) {
            structFilters += block
        }

        public fun enums(block: (info: CxEnumInfo) -> Boolean) {
            enumFilters += block
        }

        public fun functions(block: (info: CxFunctionInfo) -> Boolean) {
            functionFilters += block
        }

        public fun includeHeaders(headers: Set<String>): Unit = headers { it.name.value in headers }
        public fun includeHeaders(headers: List<String>): Unit = includeHeaders(headers.toSet())
        public fun includeHeaders(vararg headers: String): Unit = includeHeaders(headers.toSet())

        internal fun applyTo(index: CxIndex): CxIndex {
            fun <T> List<(T) -> Boolean>.matches(value: T): Boolean = all { it(value) }
            fun CxHeaderInfo.applyFilters(): CxHeaderInfo = CxHeaderInfo(
                name = name,
                typedefs = typedefs.filter(typedefFilters::matches),
                structs = structs.filter(structFilters::matches),
                enums = enums.filter(enumFilters::matches),
                functions = functions.filter(functionFilters::matches),
            )

            return CxIndex(
                builtIn = when (headerFilters.matches(index.builtIn)) {
                    true -> index.builtIn.applyFilters()
                    else -> CxHeaderInfo(CxHeaderName.BuiltIn)
                },
                headers = index.headers
                    .filter(headerFilters::matches)
                    .map(CxHeaderInfo::applyFilters)
                    .filter(CxHeaderInfo::isNotEmpty)
            )
        }
    }

    private class Declarations(
        val typedefs: Map<CxDeclarationId, CxTypedefInfo>,
        val structs: Map<CxDeclarationId, CxStructInfo>,
        val enums: Map<CxDeclarationId, CxEnumInfo>,
        val functions: Map<CxDeclarationId, CxFunctionInfo>,
    )
}
