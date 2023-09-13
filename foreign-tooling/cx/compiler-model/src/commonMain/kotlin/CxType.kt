package dev.whyoleg.foreign.tooling.cx.compiler.model

import kotlinx.serialization.*

// TODO: custom serializer?
@Serializable
public sealed class CxType {
    @Serializable
    public data class Builtin(val type: CxBuiltinType) : CxType()

    @Serializable
    public data object Void : CxType()

    @Serializable
    public data object Boolean : CxType()

    @Serializable
    public data class Pointer(val pointed: CxType) : CxType()

    @Serializable
    public data class Enum(val id: CxDeclarationId) : CxType()

    @Serializable
    public data class Typedef(val id: CxDeclarationId) : CxType()

    @Serializable
    public data class Record(val id: CxDeclarationId) : CxType()

    @Serializable
    public data class Function(val returnType: CxType, val parameters: List<CxType>) : CxType()

    @Serializable
    public sealed class Array : CxType() {
        public abstract val elementType: CxType
    }

    @Serializable
    public data class ConstArray(override val elementType: CxType, val size: kotlin.Long) : Array()

    @Serializable
    public data class IncompleteArray(override val elementType: CxType) : Array()

    @Serializable
    public data class Unknown(val name: String, val kind: String) : CxType()
}
