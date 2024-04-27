package dev.whyoleg.foreign.bridgegen

import dev.whyoleg.foreign.bridge.c.*
import dev.whyoleg.foreign.clang.api.*

public fun generateCFragment(index: CxIndex, packageName: String): CFragment {
    fun declarationId(name: String): CDeclarationId = "$packageName/$name"
    fun anonymousDeclarationId(name: String, index: Int): CDeclarationId = "$packageName/$name|$index"

    val anonymousRecordIds = buildMap {
        index.records.forEach {
            it.definition?.anonymousRecords?.toList()?.forEachIndexed { index, (id, _) ->
                put(id, anonymousDeclarationId(it.description.name, index))
            }
        }
    }

    fun CxDeclarationDescription.toCDeclarationDescription(): CDeclarationDescription = CDeclarationDescription(
        id = declarationId(name),
        packageName = packageName,
        headerName = header,
        ktName = name,
        cName = name,
        availableOn = null
    )

    fun CxType.toCType(): CType = when (this) {
        CxType.Void           -> CType.Void
        CxType.Bool           -> CType.Boolean
        is CxType.Number      -> CType.Number(CNumber.Int) // TODO

        is CxType.Enum        -> {
            val enum = index.enums.first { it.description.id == id }
            CType.Enum(declarationId(enum.description.name))
        }

        is CxType.Typedef     -> {
            val typedef = index.typedefs.first { it.description.id == id }
            CType.Typedef(declarationId(typedef.description.name))
        }

        is CxType.Record      -> {
            when (val anonymousId = anonymousRecordIds[id]) {
                null -> {
                    val record = index.records.first { it.description.id == id }
                    CType.Record(declarationId(record.description.name))
                }

                else -> CType.Record(anonymousId)
            }
        }

        is CxType.Pointer     -> CType.Pointer(pointed.toCType())
        is CxType.Array       -> CType.Array(elementType.toCType(), size)
        is CxType.Function    -> CType.Function(returnType.toCType(), parameters.map(CxType::toCType))

        is CxType.Unsupported -> CType.Unsupported(info)
    }

    fun CxEnumConstant.toCEnumConstant(): CEnumConstant = CEnumConstant(
        ktName = name,
        cName = name,
        value = value
    )

    fun CxRecordField.toCRecordField(): CRecordField = CRecordField(
        ktName = name,
        cName = name,
        fieldType = fieldType.toCType(),
        bitWidth = bitWidth
    )

    fun CxRecordDefinition.toCRecordDefinition(): CRecordDefinition = CRecordDefinition(
        isUnion = isUnion,
        size = size,
        align = align,
        fields = fields.map(CxRecordField::toCRecordField),
        anonymousRecords = buildMap {
            anonymousRecords.forEach { (id, definition) ->
                put(anonymousRecordIds.getValue(id), definition.toCRecordDefinition())
            }
        }
    )

    fun CxFunctionParameter.toCFunctionParameter(): CFunctionParameter = CFunctionParameter(
        ktName = name,
        cNames = listOfNotNull(name),
        type = type.toCType(),
    )

    return CFragment(
        variables = index.variables.map { declaration ->
            CVariable(
                description = declaration.description.toCDeclarationDescription(),
                variableType = declaration.variableType.toCType()
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
                parameters = declaration.parameters.map(CxFunctionParameter::toCFunctionParameter)
            )
        }
    )
}
