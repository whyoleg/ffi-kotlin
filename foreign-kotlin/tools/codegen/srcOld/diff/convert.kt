package dev.whyoleg.foreign.bridgegen

import dev.whyoleg.foreign.bridge.c.*
import dev.whyoleg.foreign.tool.clang.api.*

public fun generateCFragment(index: CxIndex, packageName: String): CFragment {
    fun declarationId(name: String): CDeclarationId = "$packageName/$name"
    fun anonymousDeclarationId(name: String, index: Int): CDeclarationId = "$packageName/$name|$index"

    val enumMapping = index.enums.associate {
        it.description.id to declarationId(it.description.name)
    }
    val typedefMapping = index.typedefs.associate {
        it.description.id to declarationId(it.description.name)
    }
    val recordMapping = index.records.associate {
        it.description.id to declarationId(it.description.name)
    }
    val anonymousRecordMapping = buildMap {
        index.records.forEach {
            it.definition?.anonymousRecords?.toList()?.forEachIndexed { index, (id, _) ->
                put(id, anonymousDeclarationId(it.description.name, index))
            }
        }
    }

    fun CxDeclarationDescription.toCDeclarationDescription(): CDeclarationDescription = CDeclarationDescription(
        id = declarationId(name),
        packageName = packageName,
        headerName = if (header.isEmpty()) {
            header
        } else {
            require(header.endsWith(".h") || header.endsWith(".inl")) { "failed: $header" }
            header.substringBeforeLast(".")
        },
        ktName = name,
        cNames = listOf(name),
        availableOn = null
    )

    fun CxType.toCType(): CType = when (this) {
        CxType.Void           -> CType.Void
//        CxType.Bool           -> CType.Boolean
        is CxType.Number  -> when (value) {
            CxNumber.Char, CxNumber.SignedChar -> CNumber.Byte
            CxNumber.UnsignedChar              -> CNumber.UByte
            CxNumber.Short                     -> CNumber.Short
            CxNumber.UnsignedShort             -> CNumber.UShort
            CxNumber.Int                       -> CNumber.Int
            CxNumber.UnsignedInt               -> CNumber.UInt
            CxNumber.Long                      -> CNumber.PlatformInt
            CxNumber.UnsignedLong              -> CNumber.PlatformUInt
            CxNumber.LongLong                  -> CNumber.Long
            CxNumber.UnsignedLongLong          -> CNumber.ULong
            CxNumber.Int128                    -> null
            CxNumber.UnsignedInt128            -> null
            CxNumber.Float                     -> CNumber.Float
            CxNumber.Double                    -> CNumber.Double
            CxNumber.LongDouble                -> null
        }?.let(CType::Number) ?: CType.Unsupported("Unsupported number $value")

        is CxType.Enum    -> CType.Enum(enumMapping.getValue(id))
        is CxType.Typedef -> CType.Typedef(typedefMapping.getValue(id))
        is CxType.Record  -> CType.Record(anonymousRecordMapping[id] ?: recordMapping.getValue(id))

        is CxType.Pointer     -> CType.Pointer(pointed.toCType())
        is CxType.Array       -> CType.Array(elementType.toCType(), size)
//        is CxType.Function    -> CType.Function(returnType.toCType(), parameters.map(CxType::toCType))

        is CxType.Unsupported -> CType.Unsupported(info)
    }

    fun CxEnumConstant.toCEnumConstant(): CEnumConstant = CEnumConstant(
        ktName = name,
        cNames = listOf(name),
        value = value
    )

    fun CxRecordDefinition.toCRecordDefinition(): CRecordDefinition = CRecordDefinition(
        isUnion = isUnion,
        layout = CRecordLayout(byteSize, byteAlignment),
        fields = buildList {
            fields.forEach {
                // unsupported for now
                if (it.bitWidth != null) return@forEach
                val name = it.name ?: return@forEach
                add(
                    CRecordField(
                        ktName = name,
                        cNames = listOf(name),
                        type = it.type.toCType(),
                        offset = it.bitOffset / 8
                    )
                )
            }
        },
        anonymousRecords = buildMap {
            anonymousRecords.forEach { (id, definition) ->
                put(anonymousRecordMapping.getValue(id), definition.toCRecordDefinition())
            }
        }
    )

    fun CxFunctionParameter.toCFunctionParameter(ktName: String): CFunctionParameter = CFunctionParameter(
        ktName = ktName,
        cNames = listOfNotNull(name),
        type = type.toCType(),
    )

    return CFragment(
        variables = index.variables.map { declaration ->
            CVariable(
                description = declaration.description.toCDeclarationDescription(),
                type = declaration.type.toCType()
            )
        },
        enums = index.enums.map { declaration ->
            CEnum(
                description = declaration.description.toCDeclarationDescription(),
                constants = declaration.constants.map(CxEnumConstant::toCEnumConstant)
            )
        },
        typedefs = index.typedefs.map { declaration ->
            CTypedef(
                description = declaration.description.toCDeclarationDescription(),
                aliasedType = declaration.aliasedType.toCType(),
                resolvedType = declaration.resolvedType.toCType()
            )
        },
        records = index.records.map { declaration ->
            CRecord(
                description = declaration.description.toCDeclarationDescription(),
                definition = declaration.definition?.toCRecordDefinition()
            )
        },
        functions = index.functions.map { declaration ->
            CFunction(
                description = declaration.description.toCDeclarationDescription(),
                isVariadic = declaration.isVariadic,
                returnType = declaration.returnType.toCType(),
                parameters = buildList {
                    if (declaration.parameters.any { it.name == null }) {
                        val parameterNames =
                            declaration.parameters.mapNotNullTo(mutableSetOf(), CxFunctionParameter::name)
                        var i = 0
                        declaration.parameters.forEach {
                            var ktName = "p${i++}"
                            while (ktName in parameterNames) ktName = "p${i++}"
                            add(it.toCFunctionParameter(ktName))
                        }
                    } else {
                        declaration.parameters.forEach {
                            add(it.toCFunctionParameter(it.name!!))
                        }
                    }
                }
            )
        }
    )
}
