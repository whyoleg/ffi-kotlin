package dev.whyoleg.foreign.cx.index

import kotlinx.serialization.*

//TODO: add parameters with which index was generated?
@Serializable
public data class CxIndex(
    val headers: List<CxHeaderInfo> = emptyList()
) {
    private val declarations: Declarations by lazy {
        val typedefs = mutableMapOf<CxDeclarationId, Pair<CxHeaderInfo, CxTypedefInfo>>()
        val records = mutableMapOf<CxDeclarationId, Pair<CxHeaderInfo, CxRecordInfo>>()
        val enums = mutableMapOf<CxDeclarationId, Pair<CxHeaderInfo, CxEnumInfo>>()
        val functions = mutableMapOf<CxDeclarationId, Pair<CxHeaderInfo, CxFunctionInfo>>()
        headers.forEach { header ->
            header.typedefs.forEach { typedefs[it.id] = header to it }
            header.records.forEach { records[it.id] = header to it }
            header.enums.forEach { enums[it.id] = header to it }
            header.functions.forEach { functions[it.id] = header to it }
        }
        Declarations(typedefs, records, enums, functions)
    }

    public fun typedefWithHeader(id: CxDeclarationId): Pair<CxHeaderInfo, CxTypedefInfo> = declarations.typedefs.getValue(id)

    //TODO!!!
    public fun typedef(id: CxDeclarationId): CxTypedefInfo = typedefWithHeader(id).second
    public fun record(id: CxDeclarationId): CxRecordInfo = declarations.records.getValue(id).second
    public fun enum(id: CxDeclarationId): CxEnumInfo = declarations.enums.getValue(id).second
    public fun function(id: CxDeclarationId): CxFunctionInfo = declarations.functions.getValue(id).second

    public fun filter(block: Filter.() -> Unit): CxIndex = Filter().apply(block).applyTo(this).dropEmptyHeaders()

    public class Filter internal constructor() {
        private val headerIncludeFilters = mutableListOf<HeaderFilter>()
        private val headerExcludeFilters = mutableListOf<HeaderFilter>()

        private val typedefIncludeFilters = mutableListOf<DeclarationIncludeFilter<CxTypedefInfo>>()
        private val typedefExcludeFilters = mutableListOf<DeclarationExcludeFilter<CxTypedefInfo>>()

        private val recordIncludeFilters = mutableListOf<DeclarationIncludeFilter<CxRecordInfo>>()
        private val recordExcludeFilters = mutableListOf<DeclarationExcludeFilter<CxRecordInfo>>()

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

        public fun includeTypedefs(recursive: Boolean = true, predicate: DeclarationPredicate<CxTypedefInfo>) {
            typedefIncludeFilters += DeclarationIncludeFilter(recursive, predicate)
        }

        public fun excludeTypedefs(predicate: DeclarationPredicate<CxTypedefInfo>) {
            typedefExcludeFilters += DeclarationExcludeFilter(predicate)
        }

        public fun includeRecords(recursive: Boolean = true, predicate: DeclarationPredicate<CxRecordInfo>) {
            recordIncludeFilters += DeclarationIncludeFilter(recursive, predicate)
        }

        public fun excludeRecords(predicate: DeclarationPredicate<CxRecordInfo>) {
            recordExcludeFilters += DeclarationExcludeFilter(predicate)
        }

        public fun includeEnums(recursive: Boolean = true, predicate: DeclarationPredicate<CxEnumInfo>) {
            enumIncludeFilters += DeclarationIncludeFilter(recursive, predicate)
        }

        public fun excludeEnums(predicate: DeclarationPredicate<CxEnumInfo>) {
            enumExcludeFilters += DeclarationExcludeFilter(predicate)
        }

        public fun includeFunctions(recursive: Boolean = true, predicate: DeclarationPredicate<CxFunctionInfo>) {
            functionIncludeFilters += DeclarationIncludeFilter(recursive, predicate)
        }

        public fun excludeFunctions(predicate: DeclarationPredicate<CxFunctionInfo>) {
            functionExcludeFilters += DeclarationExcludeFilter(predicate)
        }

        internal fun applyTo(index: CxIndex): CxIndex = index.applyFilters()

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
                        if (filter.predicate.matches(this, element)) return@iterating
                    }

                    includeFilters.forEach filtering@{ filter ->
                        if (!filter.predicate.matches(this, element)) return@filtering // go to next filter
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
                    declarations = records,
                    includeFilters = recordIncludeFilters,
                    excludeFilters = recordExcludeFilters,
                    addElement = Ids::addRecord
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
                records = records.filter { ids.hasRecord(it.id) },
                enums = enums.filter { ids.hasEnum(it.id) },
                functions = functions.filter { ids.hasFunction(it.id) },
            )

            headers.forEach(CxHeaderInfo::collect)

            return CxIndex(headers = headers.map(CxHeaderInfo::applyFilter))
        }

        private class DeclarationIncludeFilter<T : CxDeclarationInfo>(
            val recursive: Boolean,
            val predicate: DeclarationPredicate<T>
        )

        private class DeclarationExcludeFilter<T : CxDeclarationInfo>(
            val predicate: DeclarationPredicate<T>
        )

        private class HeaderFilter(
            val predicate: (CxHeaderInfo) -> Boolean
        )

        private class Ids(private val index: CxIndex) {
            private val typedefs = mutableSetOf<CxDeclarationId>()
            private val enums = mutableSetOf<CxDeclarationId>()
            private val records = mutableSetOf<CxDeclarationId>()
            private val functions = mutableSetOf<CxDeclarationId>()

            private val recursiveIds = mutableSetOf<CxDeclarationId>()

            fun hasTypedef(id: CxDeclarationId) = id in typedefs
            fun hasEnum(id: CxDeclarationId) = id in enums
            fun hasRecord(id: CxDeclarationId) = id in records
            fun hasFunction(id: CxDeclarationId) = id in functions

            fun addTypedef(typedef: CxTypedefInfo, recursive: Boolean) {
                val id = typedef.id

                typedefs += id

                if (!recursive) return
                if (id in recursiveIds) return

                recursiveIds += id

                addTypeRecursive(typedef.aliased.type)
            }

            @Suppress("UNUSED_PARAMETER")
            fun addEnum(enum: CxEnumInfo, recursive: Boolean) {
                enums += enum.id
            }

            fun addRecord(record: CxRecordInfo, recursive: Boolean) {
                val id = record.id

                records += id

                if (!recursive) return
                if (id in recursiveIds) return

                recursiveIds += id

                record.members?.fields?.forEach { field ->
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
                is CxType.Record   -> addRecord(index.record(type.id), recursive = true)
                is CxType.Typedef  -> addTypedef(index.typedef(type.id), recursive = true)
                else               -> Unit
            }
        }
    }

    public fun inlineTypedefs(predicate: DeclarationPredicate<CxTypedefInfo>): CxIndex {
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
                    predicate.matches(header, typedef) -> typedef.aliased.type.inlineTypedefs()
                    else                               -> this
                }
            }
            else                      -> this
        }

        fun CxTypeInfo.inlineTypedefs(): CxTypeInfo = copy(type = type.inlineTypedefs())

        fun CxHeaderInfo.inlineTypedefs(): CxHeaderInfo = CxHeaderInfo(
            name = name,
            typedefs = typedefs.filter { !predicate.matches(this, it) }.map { typedef ->
                typedef.copy(aliased = typedef.aliased.inlineTypedefs())
            },
            records = records.map { record ->
                record.copy(members = record.members?.copy(fields = record.members.fields.map { field ->
                    field.copy(type = field.type.inlineTypedefs())
                }))
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

        return CxIndex(headers = headers.map(CxHeaderInfo::inlineTypedefs)).dropEmptyHeaders()
    }

    //TODO: handle recursion
    public fun excludeUnsupportedDeclarations(): CxIndex {
        fun CxType.hasFunctionArgument(): Boolean = when (this) {
            is CxType.Function -> true
            is CxType.Array    -> elementType.hasFunctionArgument()
            is CxType.Pointer  -> pointed.hasFunctionArgument()
            is CxType.Record   -> record(id).members?.fields?.any { it.type.type.hasFunctionArgument() } ?: false
            is CxType.Typedef  -> typedef(id).aliased.type.hasFunctionArgument()
            else               -> false
        }

        fun CxHeaderInfo.excludeFunctionArguments(): CxHeaderInfo = CxHeaderInfo(
            name = name,
            typedefs = typedefs.filterNot { it.aliased.type.hasFunctionArgument() },
            records = records.filterNot { it.members?.fields?.any { it.type.type.hasFunctionArgument() } ?: false },
            enums = enums,
            functions = functions.filterNot { function ->
                function.returnType.type.hasFunctionArgument() || function.parameters.any { it.type.type.hasFunctionArgument() }
            }
        )

        return CxIndex(headers = headers.map(CxHeaderInfo::excludeFunctionArguments)).dropEmptyHeaders()
    }

    private fun CxIndex.dropEmptyHeaders(): CxIndex = CxIndex(headers.filter(CxHeaderInfo::isNotEmpty))

    private class Declarations(
        val typedefs: Map<CxDeclarationId, Pair<CxHeaderInfo, CxTypedefInfo>>,
        val records: Map<CxDeclarationId, Pair<CxHeaderInfo, CxRecordInfo>>,
        val enums: Map<CxDeclarationId, Pair<CxHeaderInfo, CxEnumInfo>>,
        val functions: Map<CxDeclarationId, Pair<CxHeaderInfo, CxFunctionInfo>>,
    )

    public companion object
}
