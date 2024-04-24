package dev.whyoleg.foreign.clang.api

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable//(CxTypeSerializer::class)
public sealed class CxType {
    @SerialName("void")
    @Serializable
    public data object Void : CxType()

    @SerialName("bool")
    @Serializable
    public data object Bool : CxType()

    @SerialName("number")
    @Serializable
    public data class Number(val value: CxNumber) : CxType()

    @SerialName("pointer")
    @Serializable
    public data class Pointer(val pointed: CxType) : CxType()

    @SerialName("array")
    @Serializable
    public data class Array(val elementType: CxType, val size: Long?) : CxType()

    @SerialName("function")
    @Serializable
    public data class Function(val returnType: CxType, val parameters: List<CxType>) : CxType()

    @SerialName("typedef")
    @Serializable
    public data class Typedef(val id: CxDeclarationId) : CxType()

    @SerialName("enum")
    @Serializable
    public data class Enum(val id: CxDeclarationId) : CxType()

    @SerialName("record")
    @Serializable
    public data class Record(val id: CxDeclarationId) : CxType()

    @SerialName("unsupported")
    @Serializable
    public data class Unsupported(val info: String) : CxType()
}

private object CxTypeSerializer : KSerializer<CxType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CxType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: CxType) {
        encoder.encodeString(encode(value))
    }

    override fun deserialize(decoder: Decoder): CxType {
        TODO("may be implement later")
    }

    private fun encode(value: CxType): String = when (value) {
        CxType.Void           -> "void"
        CxType.Bool           -> "bool"
        is CxType.Number      -> "number(${value.value})"
        is CxType.Enum        -> "enum(${value.id})"
        is CxType.Typedef     -> "typedef(${value.id})"
        is CxType.Record      -> "record(${value.id})"
        is CxType.Pointer     -> "pointer(${encode(value.pointed)})"
        is CxType.Array       -> "array(${encode(value.elementType)},${value.size})"
        is CxType.Function    -> "function(${encode(value.returnType)},${
            value.parameters.joinToString(",", transform = ::encode)
        })"

        is CxType.Unsupported -> "unsupported(${value.info})"
    }
}
