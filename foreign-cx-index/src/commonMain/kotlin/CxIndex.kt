package dev.whyoleg.foreign.cx.index

import kotlinx.serialization.*

//TODO: drop builtIn!
@Serializable
public data class CxIndex(
    val builtIn: CxHeaderInfo,
    val headers: List<CxHeaderInfo> = emptyList()
) {
    private val declarations: Declarations by lazy {
        val typedefs = mutableMapOf<CxDeclarationId, Pair<CxHeaderInfo, CxTypedefInfo>>()
        val structs = mutableMapOf<CxDeclarationId, Pair<CxHeaderInfo, CxStructInfo>>()
        val enums = mutableMapOf<CxDeclarationId, Pair<CxHeaderInfo, CxEnumInfo>>()
        val functions = mutableMapOf<CxDeclarationId, Pair<CxHeaderInfo, CxFunctionInfo>>()
        (headers + builtIn).forEach { header ->
            header.typedefs.forEach { typedefs[it.id] = header to it }
            header.structs.forEach { structs[it.id] = header to it }
            header.enums.forEach { enums[it.id] = header to it }
            header.functions.forEach { functions[it.id] = header to it }
        }
        Declarations(typedefs, structs, enums, functions)
    }

    public fun typedefWithHeader(id: CxDeclarationId): Pair<CxHeaderInfo, CxTypedefInfo> = declarations.typedefs.getValue(id)

    //TODO!!!
    public fun typedef(id: CxDeclarationId): CxTypedefInfo = typedefWithHeader(id).second
    public fun struct(id: CxDeclarationId): CxStructInfo = declarations.structs.getValue(id).second
    public fun enum(id: CxDeclarationId): CxEnumInfo = declarations.enums.getValue(id).second
    public fun function(id: CxDeclarationId): CxFunctionInfo = declarations.functions.getValue(id).second

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

        private val typedefInlinePredicates = mutableListOf<TypedefInlinePredicate>()

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

        //TODO: may be provide just CxHeaderName?
        public fun includeTypedefs(recursive: Boolean = true, block: (header: CxHeaderInfo, info: CxTypedefInfo) -> Boolean) {
            typedefIncludeFilters += DeclarationIncludeFilter(recursive, block)
        }

        public fun excludeTypedefs(block: (header: CxHeaderInfo, info: CxTypedefInfo) -> Boolean) {
            typedefExcludeFilters += DeclarationExcludeFilter(block)
        }

        public fun includeStructs(recursive: Boolean = true, block: (header: CxHeaderInfo, info: CxStructInfo) -> Boolean) {
            structIncludeFilters += DeclarationIncludeFilter(recursive, block)
        }

        public fun excludeStructs(block: (header: CxHeaderInfo, info: CxStructInfo) -> Boolean) {
            structExcludeFilters += DeclarationExcludeFilter(block)
        }

        public fun includeEnums(recursive: Boolean = true, block: (header: CxHeaderInfo, info: CxEnumInfo) -> Boolean) {
            enumIncludeFilters += DeclarationIncludeFilter(recursive, block)
        }

        public fun excludeEnums(block: (header: CxHeaderInfo, info: CxEnumInfo) -> Boolean) {
            enumExcludeFilters += DeclarationExcludeFilter(block)
        }

        public fun includeFunctions(recursive: Boolean = true, block: (header: CxHeaderInfo, info: CxFunctionInfo) -> Boolean) {
            functionIncludeFilters += DeclarationIncludeFilter(recursive, block)
        }

        public fun excludeFunctions(block: (header: CxHeaderInfo, info: CxFunctionInfo) -> Boolean) {
            functionExcludeFilters += DeclarationExcludeFilter(block)
        }

        public fun inlineTypedefs(block: (header: CxHeaderInfo, info: CxTypedefInfo) -> Boolean) {
            typedefInlinePredicates += TypedefInlinePredicate(block)
        }

        internal fun applyTo(index: CxIndex): CxIndex = index
            .applyFilters()
            .inlineTypedefs()
            .run { CxIndex(builtIn, headers.filter(CxHeaderInfo::isNotEmpty)) }

        private fun CxIndex.applyFilters(): CxIndex {
            val ids = Ids(this)

            fun <T : CxDeclarationInfo> CxHeaderInfo.collect(
                declarations: List<T>,
                includeFilters: List<DeclarationIncludeFilter<T>>,
                excludeFilters: List<DeclarationExcludeFilter<T>>,
                addElement: Ids.(element: T, recursive: Boolean) -> Unit,
            ) {
                declarations.forEach iterating@{ element ->
                    excludeFilters.forEach { filter ->
                        //matching exclude filter, means declaration will not be included at all, so no reasons to check `include` filters
                        if (filter.predicate(this, element)) return@iterating
                    }

                    includeFilters.forEach filtering@{ filter ->
                        if (!filter.predicate(this, element)) return@filtering // go to next filter
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

            (headers + builtIn).forEach(CxHeaderInfo::collect)

            return CxIndex(
                builtIn = builtIn.applyFilter(),
                headers = headers.map(CxHeaderInfo::applyFilter)
            )
        }

        private fun CxIndex.inlineTypedefs(): CxIndex {
            if (typedefInlinePredicates.isEmpty()) return this

            fun CxTypedefInfo.needInline(header: CxHeaderInfo): Boolean {
                typedefInlinePredicates.forEach {
                    if (it.predicate(header, this)) return true
                }
                return false
            }

            fun CxType.inlineTypedefs(): CxType = when (this) {
                is CxType.ConstArray      -> copy(elementType = elementType.inlineTypedefs())
                is CxType.IncompleteArray -> copy(elementType = elementType.inlineTypedefs())
                is CxType.Pointer         -> copy(pointed = pointed.inlineTypedefs())
                is CxType.Function        -> copy(
                    returnType = returnType.inlineTypedefs(),
                    parameters = parameters.map(CxType::inlineTypedefs)
                )
                is CxType.Typedef         -> {
                    val (header, typedef) = typedefWithHeader(id)
                    when {
                        typedef.needInline(header) -> typedef.aliased.type.inlineTypedefs()
                        else                       -> this
                    }
                }
                else                      -> this
            }

            fun CxTypeInfo.inlineTypedefs(): CxTypeInfo = copy(type = type.inlineTypedefs())

            fun CxHeaderInfo.inlineTypedefs(): CxHeaderInfo = CxHeaderInfo(
                name = name,
                typedefs = typedefs.filter { !it.needInline(this) },
                structs = structs.map { struct ->
                    struct.copy(fields = struct.fields.map { field ->
                        field.copy(type = field.type.inlineTypedefs())
                    })
                },
                enums = enums,
                functions = functions.map { function ->
                    function.copy(
                        returnType = function.returnType.inlineTypedefs(),
                        parameters = function.parameters.map { parameter ->
                            parameter.copy(type = parameter.type.inlineTypedefs())
                        }
                    )
                },
            )

            return CxIndex(
                builtIn = builtIn.inlineTypedefs(),
                headers = headers.map(CxHeaderInfo::inlineTypedefs)
            )
        }

        private class TypedefInlinePredicate(
            val predicate: (header: CxHeaderInfo, info: CxTypedefInfo) -> Boolean
        )

        private class DeclarationIncludeFilter<T : CxDeclarationInfo>(
            val recursive: Boolean,
            val predicate: (CxHeaderInfo, T) -> Boolean
        )

        private class DeclarationExcludeFilter<T : CxDeclarationInfo>(
            val predicate: (CxHeaderInfo, T) -> Boolean
        )

        private class HeaderFilter(
            val predicate: (CxHeaderInfo) -> Boolean
        )

        private class Ids(private val index: CxIndex) {
            private val typedefs = mutableSetOf<CxDeclarationId>()
            private val enums = mutableSetOf<CxDeclarationId>()
            private val structs = mutableSetOf<CxDeclarationId>()
            private val functions = mutableSetOf<CxDeclarationId>()

            private val recursiveIds = mutableSetOf<CxDeclarationId>()

            fun hasTypedef(id: CxDeclarationId) = id in typedefs
            fun hasEnum(id: CxDeclarationId) = id in enums
            fun hasStruct(id: CxDeclarationId) = id in structs
            fun hasFunction(id: CxDeclarationId) = id in functions

            fun addTypedef(typedef: CxTypedefInfo, recursive: Boolean) {
                val id = typedef.id

                typedefs += id

                if (!recursive) return
                if (id in recursiveIds) return

                recursiveIds += id

                addTypeRecursive(typedef.aliased.type)
            }

            fun addEnum(enum: CxEnumInfo, recursive: Boolean) {
                enums += enum.id
            }

            fun addStruct(struct: CxStructInfo, recursive: Boolean) {
                val id = struct.id

                structs += id

                if (!recursive) return
                if (id in recursiveIds) return

                recursiveIds += id

                struct.fields.forEach { field ->
                    addTypeRecursive(field.type.type)
                }
            }

            fun addFunction(function: CxFunctionInfo, recursive: Boolean) {
                val id = function.id
                functions += id

                if (!recursive) return
                if (id in recursiveIds) return

                recursiveIds += id

                addTypeRecursive(function.returnType.type)
                function.parameters.forEach { parameter ->
                    addTypeRecursive(parameter.type.type)
                }
            }

            private tailrec fun addTypeRecursive(type: CxType): Unit = when (type) {
                is CxType.Array    -> addTypeRecursive(type.elementType)
                is CxType.Pointer  -> addTypeRecursive(type.pointed)
                is CxType.Function -> (type.parameters + type.returnType).forEach(::addTypeRecursive)
                is CxType.Enum     -> addEnum(index.enum(type.id), recursive = true)
                is CxType.Struct   -> addStruct(index.struct(type.id), recursive = true)
                is CxType.Typedef  -> addTypedef(index.typedef(type.id), recursive = true)
                else               -> Unit
            }
        }
    }

    private class Declarations(
        val typedefs: Map<CxDeclarationId, Pair<CxHeaderInfo, CxTypedefInfo>>,
        val structs: Map<CxDeclarationId, Pair<CxHeaderInfo, CxStructInfo>>,
        val enums: Map<CxDeclarationId, Pair<CxHeaderInfo, CxEnumInfo>>,
        val functions: Map<CxDeclarationId, Pair<CxHeaderInfo, CxFunctionInfo>>,
    )
}
