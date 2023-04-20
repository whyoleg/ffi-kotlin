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
        private val headerIncludeFilters = mutableListOf<HeaderFilter>()
        private val headerExcludeFilters = mutableListOf<HeaderFilter>()

        private val typedefIncludeFilters = mutableListOf<DeclarationIncludeFilter<CxTypedefInfo>>()
        private val typedefExcludeFilters = mutableListOf<DeclarationExcludeFilter<CxTypedefInfo>>()

        private val structIncludeFilters = mutableListOf<DeclarationIncludeFilter<CxStructInfo>>()
        private val structExcludeFilters = mutableListOf<DeclarationExcludeFilter<CxStructInfo>>()

        private val enumIncludeFilters = mutableListOf<DeclarationIncludeFilter<CxEnumInfo>>()
        private val enumExcludeFilters = mutableListOf<DeclarationExcludeFilter<CxEnumInfo>>()

        private val functionIncludeFilters = mutableListOf<DeclarationIncludeFilter<CxFunctionInfo>>()
        private val functionExcludeFilters = mutableListOf<DeclarationExcludeFilter<CxFunctionInfo>>()

        public fun includeHeaders(block: (info: CxHeaderInfo) -> Boolean) {
            headerIncludeFilters += HeaderFilter(block)
        }

        public fun includeHeaders(headers: Set<String>): Unit = includeHeaders { it.name.value in headers }
        public fun includeHeaders(headers: List<String>): Unit = includeHeaders(headers.toSet())
        public fun includeHeaders(vararg headers: String): Unit = includeHeaders(headers.toSet())

        public fun excludeHeaders(block: (info: CxHeaderInfo) -> Boolean) {
            headerExcludeFilters += HeaderFilter(block)
        }

        public fun excludeHeaders(headers: Set<String>): Unit = excludeHeaders { it.name.value !in headers }
        public fun excludeHeaders(headers: List<String>): Unit = excludeHeaders(headers.toSet())
        public fun excludeHeaders(vararg headers: String): Unit = excludeHeaders(headers.toSet())

        public fun includeTypedefs(recursive: Boolean = true, block: (info: CxTypedefInfo) -> Boolean) {
            typedefIncludeFilters += DeclarationIncludeFilter(recursive, block)
        }

        public fun excludeTypedefs(block: (info: CxTypedefInfo) -> Boolean) {
            typedefExcludeFilters += DeclarationExcludeFilter(block)
        }

        public fun includeStructs(recursive: Boolean = true, block: (info: CxStructInfo) -> Boolean) {
            structIncludeFilters += DeclarationIncludeFilter(recursive, block)
        }

        public fun excludeStructs(block: (info: CxStructInfo) -> Boolean) {
            structExcludeFilters += DeclarationExcludeFilter(block)
        }

        public fun includeEnums(recursive: Boolean = true, block: (info: CxEnumInfo) -> Boolean) {
            enumIncludeFilters += DeclarationIncludeFilter(recursive, block)
        }

        public fun excludeEnums(block: (info: CxEnumInfo) -> Boolean) {
            enumExcludeFilters += DeclarationExcludeFilter(block)
        }

        public fun includeFunctions(recursive: Boolean = true, block: (info: CxFunctionInfo) -> Boolean) {
            functionIncludeFilters += DeclarationIncludeFilter(recursive, block)
        }

        public fun excludeFunctions(block: (info: CxFunctionInfo) -> Boolean) {
            functionExcludeFilters += DeclarationExcludeFilter(block)
        }

        internal fun applyTo(index: CxIndex): CxIndex {
            val ids = Ids(index.declarations)

            fun <T : CxDeclarationInfo> collect(
                declarations: List<T>,
                includeFilters: List<DeclarationIncludeFilter<T>>,
                excludeFilters: List<DeclarationExcludeFilter<T>>,
                addElement: Ids.(element: T, recursive: Boolean) -> Unit,
            ) {
                declarations.forEach iterating@{ element ->
                    excludeFilters.forEach { filter ->
                        //matching exclude filter, means declaration will not be included at all, so no reasons to check `include` filters
                        if (filter.predicate(element)) return@iterating
                    }

                    includeFilters.forEach filtering@{ filter ->
                        if (!filter.predicate(element)) return@filtering // go to next filter
                        ids.addElement(element, filter.recursive)
                        // we added an element recursively, so there is no sense to filter it more
                        if (filter.recursive) return@iterating
                    }
                }
            }

            fun CxHeaderInfo.collect() {
                fun matches(): Boolean {
                    headerExcludeFilters.forEach { filter ->
                        if (filter.predicate(this)) return false
                    }
                    headerIncludeFilters.forEach { filter ->
                        if (filter.predicate(this)) return true
                    }

                    return true //TODO: what is default?
                }

                if (!matches()) return

                collect(
                    declarations = typedefs,
                    includeFilters = typedefIncludeFilters,
                    excludeFilters = typedefExcludeFilters,
                    addElement = Ids::addTypedef,
                )

                collect(
                    declarations = structs,
                    includeFilters = structIncludeFilters,
                    excludeFilters = structExcludeFilters,
                    addElement = Ids::addStruct
                )

                collect(
                    declarations = enums,
                    includeFilters = enumIncludeFilters,
                    excludeFilters = enumExcludeFilters,
                    addElement = Ids::addEnum
                )

                collect(
                    declarations = functions,
                    includeFilters = functionIncludeFilters,
                    excludeFilters = functionExcludeFilters,
                    addElement = Ids::addFunction
                )
            }

            fun CxHeaderInfo.applyFilter(): CxHeaderInfo = CxHeaderInfo(
                name = name,
                typedefs = typedefs.filter { ids.hasTypedef(it.id) },
                structs = structs.filter { ids.hasStruct(it.id) },
                enums = enums.filter { ids.hasEnum(it.id) },
                functions = functions.filter { ids.hasFunction(it.id) },
            )

            (index.headers + index.builtIn).forEach(CxHeaderInfo::collect)

            return CxIndex(
                builtIn = index.builtIn.applyFilter(),
                headers = index.headers
                    .map(CxHeaderInfo::applyFilter)
                    .filter(CxHeaderInfo::isNotEmpty)
            )
        }

        private class DeclarationIncludeFilter<T : CxDeclarationInfo>(
            val recursive: Boolean,
            val predicate: (T) -> Boolean
        )

        private class DeclarationExcludeFilter<T : CxDeclarationInfo>(
            val predicate: (T) -> Boolean
        )

        private class HeaderFilter(
            val predicate: (CxHeaderInfo) -> Boolean
        )
    }

    private class Declarations(
        val typedefs: Map<CxDeclarationId, CxTypedefInfo>,
        val structs: Map<CxDeclarationId, CxStructInfo>,
        val enums: Map<CxDeclarationId, CxEnumInfo>,
        val functions: Map<CxDeclarationId, CxFunctionInfo>,
    )

    private class Ids(
        private val declarations: Declarations
    ) {
        private val typedefs = mutableSetOf<CxDeclarationId>()
        private val enums = mutableSetOf<CxDeclarationId>()
        private val structs = mutableSetOf<CxDeclarationId>()
        private val functions = mutableSetOf<CxDeclarationId>()

        fun hasTypedef(id: CxDeclarationId) = id in typedefs
        fun hasEnum(id: CxDeclarationId) = id in enums
        fun hasStruct(id: CxDeclarationId) = id in structs
        fun hasFunction(id: CxDeclarationId) = id in functions

        fun addTypedef(typedef: CxTypedefInfo, recursive: Boolean) {
            typedefs += typedef.id
            if (recursive) addTypeRecursive(typedef.aliased.type)
        }

        fun addEnum(enum: CxEnumInfo, recursive: Boolean) {
            enums += enum.id
        }

        fun addStruct(struct: CxStructInfo, recursive: Boolean) {
            structs += struct.id
            if (recursive) struct.fields.forEach { field ->
                addTypeRecursive(field.type.type)
            }
        }

        fun addFunction(function: CxFunctionInfo, recursive: Boolean) {
            functions += function.id
            if (recursive) {
                addTypeRecursive(function.returnType.type)
                function.parameters.forEach { parameter ->
                    addTypeRecursive(parameter.type.type)
                }
            }
        }

        private tailrec fun addTypeRecursive(type: CxType): Unit = when (type) {
            is CxType.Array    -> addTypeRecursive(type.elementType)
            is CxType.Pointer  -> addTypeRecursive(type.pointed)
            is CxType.Function -> (type.parameters + type.returnType).forEach(::addTypeRecursive)
            is CxType.Enum     -> addEnum(declarations.enums.getValue(type.id), recursive = true)
            is CxType.Struct   -> addStruct(declarations.structs.getValue(type.id), recursive = true)
            is CxType.Typedef  -> addTypedef(declarations.typedefs.getValue(type.id), recursive = true)
            else               -> Unit
        }
    }
}
