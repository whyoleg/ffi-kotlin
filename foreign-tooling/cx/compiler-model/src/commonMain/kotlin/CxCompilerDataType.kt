package dev.whyoleg.foreign.tooling.cx.compiler.model

import kotlinx.serialization.*

@Serializable // TODO: custom serializer?
public sealed class CxCompilerDataType {
    @SerialName("primitive")
    @Serializable
    public data class Primitive(val value: CxPrimitiveDataType) : CxCompilerDataType()

    @SerialName("pointer")
    @Serializable
    public data class Pointer(val pointed: CxCompilerDataType) : CxCompilerDataType()

    @SerialName("enum")
    @Serializable
    public data class Enum(val id: CxCompilerDeclarationId) : CxCompilerDataType()

    @SerialName("typedef")
    @Serializable
    public data class Typedef(val id: CxCompilerDeclarationId) : CxCompilerDataType()

    @SerialName("record")
    @Serializable
    public data class Record(val id: CxCompilerDeclarationId) : CxCompilerDataType()

    @SerialName("function")
    @Serializable
    public data class Function(val returnType: CxCompilerDataType, val parameters: List<CxCompilerDataType>) : CxCompilerDataType()

    @Serializable
    public sealed class Array : CxCompilerDataType() {
        public abstract val elementType: CxCompilerDataType
    }

    @SerialName("constArray")
    @Serializable
    public data class ConstArray(override val elementType: CxCompilerDataType, val size: Long) : Array()

    @SerialName("incompleteArray")
    @Serializable
    public data class IncompleteArray(override val elementType: CxCompilerDataType) : Array()

    @SerialName("unsupported")
    @Serializable
    public data class Unsupported(val info: String) : CxCompilerDataType()
}
