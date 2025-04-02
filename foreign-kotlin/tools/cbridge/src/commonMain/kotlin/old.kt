package dev.whyoleg.foreign.tool.cbridge

//public fun CxBridgeFragment(
//    fragmentId: CxBridgeFragmentId,
//    fragments: Map<CxBridgeFragmentId, CxBridgeFragment>
//): CxBridgeFragment = CxBridgeFragment(
//    fragmentId = fragmentId,
//    variables = fragments.intersectionByKeys(CxBridgeFragment::variables).associateWith { id ->
//        CxBridgeVariable(
//            id = id,
//            returnType = fragments.sharedType { it.variables.getValue(id).returnType },
//        )
//    },
//    typedefs = fragments.intersectionByKeys(CxBridgeFragment::typedefs).associateWith { id ->
//        CxBridgeTypedef(
//            id = id,
//            aliasedType = fragments.sharedType { it.typedefs.getValue(id).aliasedType },
//            resolvedType = fragments.sharedType { it.typedefs.getValue(id).resolvedType },
//        )
//    },
//    enums = fragments.intersectionByKeys(CxBridgeFragment::enums).associateWith { id ->
//        val variants = fragments.mapValues { it.value.enums.getValue(id) }
//
//        val unnamed =
//            variants.map { it.value.unnamed }.toSet().singleOrNull()
//                ?: error("something bad happens: not all enums unnamed")
//
//        CxBridgeEnum(
//            id = id,
//            unnamed = unnamed,
//            constantNames = fragments.values.intersection { it.enums.getValue(id).constantNames }
//        )
//    },
//    records = fragments.intersectionByKeys(CxBridgeFragment::records).associateWith { id ->
//        val variants = fragments.mapValues { it.value.records.getValue(id) }
//
//        val isOpaque =
//            variants.map { it.value.data == null }.toSet().singleOrNull()
//                ?: error("something bad happens: not all opaque|not-opaque")
//
//
//        CxBridgeRecord(
//            id = id,
//            data = when {
//                isOpaque -> null
//                else     -> {
//                    val isUnion =
//                        variants.map { it.value.data!!.isUnion }.toSet().singleOrNull()
//                            ?: error("something bad happens: not all unions|structs")
//
//                    // TODO is it possible to commonize fields better?
//                    val fields = variants.mapValues { it.value.data!!.fields.associateBy { it.name } }
//                    CxBridgeRecordData(
//                        isUnion = isUnion,
//                        fields = fields.intersectionByKeys { it }.map { fieldName ->
//                            CxBridgeRecordData.Field(
//                                name = fieldName,
//                                type = fields.sharedType { it.getValue(fieldName).type }
//                            )
//                        }
//                    )
//                }
//            }
//        )
//    },
//    functions = fragments.intersectionByKeys(CxBridgeFragment::functions).associateWith { id ->
//        val variants = fragments.mapValues { it.value.functions.getValue(id) }
//
//        variants.map { it.value.parameters.size }.toSet().singleOrNull()
//            ?: error("something bad happens: functions has different amount of arguments")
//
//        val isVariadic = variants.map { it.value.isVariadic }.toSet().singleOrNull()
//            ?: error("something bad happens: not all functions are variadic|non-variadic")
//
//        CxBridgeFunction(
//            id = id,
//            isVariadic = isVariadic,
//            returnType = variants.sharedType { it.returnType },
//            parameters = run {
//                val params = variants.mapValues {
//                    it.value.parameters.withIndex().associate { it.index to it.value }
//                }
//                params.intersectionByKeys { it }.map { parameterIndex ->
//                    val names = params.values.map { it.getValue(parameterIndex).name }.toSet()
//                    CxBridgeFunction.Parameter(
//                        name = names.singleOrNull() ?: "p_$parameterIndex",
//                        aliasNames = if (names.size == 1) emptySet() else names,
//                        type = params.sharedType { it.getValue(parameterIndex).type }
//                    )
//                }
//            }
//        )
//    },
//)
//
//private fun <T, R> Collection<T>.intersection(selector: (T) -> Set<R>): Set<R> {
//    require(isNotEmpty()) { "no values: critical failure" }
//    var keys: Set<R>? = null
//    forEach {
//        val value = selector(it)
//        keys = keys?.intersect(value) ?: value
//    }
//    return keys ?: emptySet()
//}
//
//private fun <T, R> Map<*, T>.intersectionByKeys(selector: (T) -> Map<R, *>): Set<R> {
//    require(isNotEmpty()) { "no values: critical failure" }
//    var keys: Set<R>? = null
//    forEach {
//        val value = selector(it.value).keys
//        keys = keys?.intersect(value) ?: value
//    }
//    return keys ?: emptySet()
//}
//
//private fun <T> Map<CxBridgeFragmentId, T>.sharedType(typeProvider: (T) -> CxBridgeDataType): CxBridgeDataType {
//    val variants = mapValues { typeProvider(it.value) }
//    // TODO - may be don't merge
//    return variants.values.distinct().singleOrNull() ?: CxBridgeDataType.Shared(buildMap {
//        variants.forEach { (fragmentId, type) ->
//            when (type) {
//                is CxBridgeDataType.Shared -> putAll(type.variants)
//                else                       -> put(fragmentId, type)
//            }
//        }
//    })
//}
//@Suppress("FunctionName")
//public fun CxBridgeFragment(
//    fragmentId: CxBridgeFragmentId,
//    index: CxCompilerIndex,
//    declarationId: CxCompilerDeclaration<*>.() -> CxBridgeDeclarationId
//): Pair<CxBridgeFragment, CxBridgeFragmentMapping> {
//    fun <T : CxCompilerDeclarationData?> mapIndexDeclarations(declarations: CxCompilerDeclarations<T>) =
//        declarations.mapValues { declarationId(it.value) to it.value }.ensureNoCollisions()
//
//    val variablesMapping = mapIndexDeclarations(index.variables)
//    val enumsMapping = mapIndexDeclarations(index.enums)
//    val recordsMapping = mapIndexDeclarations(index.records)
//    val typedefsMapping = mapIndexDeclarations(index.typedefs)
//    val functionsMapping = mapIndexDeclarations(index.functions)
//
//    fun CxCompilerDataType.convert(): CxBridgeDataType = when (this) {
//        is CxCompilerDataType.Primitive -> CxBridgeDataType.Primitive(value)
//        is CxCompilerDataType.Pointer -> CxBridgeDataType.Pointer(pointed.convert())
//        is CxCompilerDataType.Enum        -> CxBridgeDataType.Enum(enumsMapping.getValue(id).first)
//        is CxCompilerDataType.Typedef     -> CxBridgeDataType.Typedef(typedefsMapping.getValue(id).first)
//        is CxCompilerDataType.Record.Reference -> CxBridgeDataType.Record.Reference(recordsMapping.getValue(id).first)
//        is CxCompilerDataType.Record.Anonymous -> CxBridgeDataType.Record.Anonymous(
//            CxBridgeRecordData(
//                isUnion = data.isUnion,
//                fields = data.fields.mapIndexed { index, field ->
//                    CxBridgeRecordData.Field(
//                        name = field.name ?: "p_$index",
//                        type = field.type.convert()
//                    )
//                }
//            )
//        )
//        is CxCompilerDataType.Function -> CxBridgeDataType.Function(
//            returnType = returnType.convert(),
//            parameters = parameters.map { it.convert() }
//        )
//        is CxCompilerDataType.Array -> CxBridgeDataType.Array(elementType.convert())
//        is CxCompilerDataType.Unsupported -> CxBridgeDataType.Unsupported(info)
//    }
//
//    return CxBridgeFragment(
//        fragmentId = fragmentId,
//        variables = variablesMapping.values.associate { (id, value) ->
//            id to CxBridgeVariable(
//                id = id,
//                returnType = value.data.returnType.convert(),
//            )
//        },
//        enums = enumsMapping.values.associate { (id, value) ->
//            id to CxBridgeEnum(
//                id = id,
//                unnamed = value.data.unnamed,
//                constantNames = value.data.constants.map { it.name }.toSet()
//            )
//        },
//        records = recordsMapping.values.associate { (id, value) ->
//            id to CxBridgeRecord(
//                id = id,
//                data = value.data?.let {
//                    CxBridgeRecordData(
//                        isUnion = it.isUnion,
//                        fields = it.fields.mapIndexed { index, field ->
//                            CxBridgeRecordData.Field(
//                                name = field.name ?: "p_$index",
//                                type = field.type.convert()
//                            )
//                        }
//                    )
//                }
//            )
//        },
//        typedefs = typedefsMapping.values.associate { (id, value) ->
//            id to CxBridgeTypedef(
//                id = id,
//                aliasedType = value.data.aliasedType.convert(),
//                resolvedType = value.data.resolvedType.convert()
//            )
//        },
//        functions = functionsMapping.values.associate { (id, value) ->
//            id to CxBridgeFunction(
//                id = id,
//                isVariadic = value.data.isVariadic,
//                returnType = value.data.returnType.convert(),
//                parameters = value.data.parameters.mapIndexed { index, parameter ->
//                    CxBridgeFunction.Parameter(
//                        name = parameter.name ?: "p_$index",
//                        aliasNames = emptySet(),
//                        type = parameter.type.convert()
//                    )
//                }
//            )
//        },
//    ) to CxBridgeFragmentMapping(
//        fragmentId = fragmentId,
//        variables = emptyMap(),
//        enums = emptyMap(),
//        records = emptyMap(),
//        typedefs = emptyMap(),
//        functions = emptyMap(),
//    )
//}
//
//private fun <T : Map<*, Pair<CxBridgeDeclarationId, *>>> T.ensureNoCollisions(): T {
//    val collisions =
//        values.groupBy { it.first }.filterValues { it.size > 1 }
//
//    check(collisions.isEmpty()) {
//        buildString {
//            appendLine("There are collisions in naming: ")
//            collisions.forEach { (id, values) ->
//                appendLine("  ID: $id")
//                values.forEach {
//                    appendLine("    ${it.second}")
//                }
//            }
//        }
//    }
//    return this
//}
//public fun generateCFragment(index: CxIndex, packageName: String): CFragment {
//    fun declarationId(name: String): CDeclarationId = "$packageName/$name"
//    fun anonymousDeclarationId(name: String, index: Int): CDeclarationId = "$packageName/$name|$index"
//
//    val enumMapping = index.enums.associate {
//        it.description.id to declarationId(it.description.name)
//    }
//    val typedefMapping = index.typedefs.associate {
//        it.description.id to declarationId(it.description.name)
//    }
//    val recordMapping = index.records.associate {
//        it.description.id to declarationId(it.description.name)
//    }
//    val anonymousRecordMapping = buildMap {
//        index.records.forEach {
//            it.definition?.anonymousRecords?.toList()?.forEachIndexed { index, (id, _) ->
//                put(id, anonymousDeclarationId(it.description.name, index))
//            }
//        }
//    }
//
//    fun CxDeclarationDescription.toCDeclarationDescription(): CDeclarationDescription = CDeclarationDescription(
//        id = declarationId(name),
//        packageName = packageName,
//        headerName = if (header.isEmpty()) {
//            header
//        } else {
//            require(header.endsWith(".h") || header.endsWith(".inl")) { "failed: $header" }
//            header.substringBeforeLast(".")
//        },
//        ktName = name,
//        cNames = listOf(name),
//        availableOn = null
//    )
//
//    fun CxType.toCType(): CType = when (this) {
//        CxType.Void           -> CType.Void
////        CxType.Bool           -> CType.Boolean
//        is CxType.Number  -> when (value) {
//            CxNumber.Char, CxNumber.SignedChar -> CNumber.Byte
//            CxNumber.UnsignedChar              -> CNumber.UByte
//            CxNumber.Short                     -> CNumber.Short
//            CxNumber.UnsignedShort             -> CNumber.UShort
//            CxNumber.Int                       -> CNumber.Int
//            CxNumber.UnsignedInt               -> CNumber.UInt
//            CxNumber.Long                      -> CNumber.PlatformInt
//            CxNumber.UnsignedLong              -> CNumber.PlatformUInt
//            CxNumber.LongLong                  -> CNumber.Long
//            CxNumber.UnsignedLongLong          -> CNumber.ULong
//            CxNumber.Int128                    -> null
//            CxNumber.UnsignedInt128            -> null
//            CxNumber.Float                     -> CNumber.Float
//            CxNumber.Double                    -> CNumber.Double
//            CxNumber.LongDouble                -> null
//        }?.let(CType::Number) ?: CType.Unsupported("Unsupported number $value")
//
//        is CxType.Enum    -> CType.Enum(enumMapping.getValue(id))
//        is CxType.Typedef -> CType.Typedef(typedefMapping.getValue(id))
//        is CxType.Record  -> CType.Record(anonymousRecordMapping[id] ?: recordMapping.getValue(id))
//
//        is CxType.Pointer     -> CType.Pointer(pointed.toCType())
//        is CxType.Array       -> CType.Array(elementType.toCType(), size)
////        is CxType.Function    -> CType.Function(returnType.toCType(), parameters.map(CxType::toCType))
//
//        is CxType.Unsupported -> CType.Unsupported(info)
//    }
//
//    fun CxEnumConstant.toCEnumConstant(): CEnumConstant = CEnumConstant(
//        ktName = name,
//        cNames = listOf(name),
//        value = value
//    )
//
//    fun CxRecordDefinition.toCRecordDefinition(): CRecordDefinition = CRecordDefinition(
//        isUnion = isUnion,
//        layout = CRecordLayout(byteSize, byteAlignment),
//        fields = buildList {
//            fields.forEach {
//                // unsupported for now
//                if (it.bitWidth != null) return@forEach
//                val name = it.name ?: return@forEach
//                add(
//                    CRecordField(
//                        ktName = name,
//                        cNames = listOf(name),
//                        type = it.type.toCType(),
//                        offset = it.bitOffset / 8
//                    )
//                )
//            }
//        },
//        anonymousRecords = buildMap {
//            anonymousRecords.forEach { (id, definition) ->
//                put(anonymousRecordMapping.getValue(id), definition.toCRecordDefinition())
//            }
//        }
//    )
//
//    fun CxFunctionParameter.toCFunctionParameter(ktName: String): CFunctionParameter = CFunctionParameter(
//        ktName = ktName,
//        cNames = listOfNotNull(name),
//        type = type.toCType(),
//    )
//
//    return CFragment(
//        variables = index.variables.map { declaration ->
//            CVariable(
//                description = declaration.description.toCDeclarationDescription(),
//                type = declaration.type.toCType()
//            )
//        },
//        enums = index.enums.map { declaration ->
//            CEnum(
//                description = declaration.description.toCDeclarationDescription(),
//                constants = declaration.constants.map(CxEnumConstant::toCEnumConstant)
//            )
//        },
//        typedefs = index.typedefs.map { declaration ->
//            CTypedef(
//                description = declaration.description.toCDeclarationDescription(),
//                aliasedType = declaration.aliasedType.toCType(),
//                resolvedType = declaration.resolvedType.toCType()
//            )
//        },
//        records = index.records.map { declaration ->
//            CRecord(
//                description = declaration.description.toCDeclarationDescription(),
//                definition = declaration.definition?.toCRecordDefinition()
//            )
//        },
//        functions = index.functions.map { declaration ->
//            CFunction(
//                description = declaration.description.toCDeclarationDescription(),
//                isVariadic = declaration.isVariadic,
//                returnType = declaration.returnType.toCType(),
//                parameters = buildList {
//                    if (declaration.parameters.any { it.name == null }) {
//                        val parameterNames =
//                            declaration.parameters.mapNotNullTo(mutableSetOf(), CxFunctionParameter::name)
//                        var i = 0
//                        declaration.parameters.forEach {
//                            var ktName = "p${i++}"
//                            while (ktName in parameterNames) ktName = "p${i++}"
//                            add(it.toCFunctionParameter(ktName))
//                        }
//                    } else {
//                        declaration.parameters.forEach {
//                            add(it.toCFunctionParameter(it.name!!))
//                        }
//                    }
//                }
//            )
//        }
//    )
//}
