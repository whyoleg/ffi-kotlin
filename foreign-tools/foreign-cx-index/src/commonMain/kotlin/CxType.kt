package dev.whyoleg.foreign.cx.index

import kotlinx.serialization.*

//TODO: decide on nullability of name and overall - is it needed, or we can just get name from type?
@Serializable
public data class CxTypeInfo(
    val name: String?,
    val type: CxType,
)

// TODO: custom serializer?
// TODO: use c names for types, dont mix with kotlin
// TODO: add a little hierarchy (like primitives, arrays, etc)?
@Serializable
public sealed class CxType {
    @Serializable
    public data object Void : CxType()

    @Serializable
    public data object Boolean : CxType()

    @Serializable
    public data object Char : CxType()

    @Serializable
    public data object Byte : CxType()

    @Serializable
    public data object UByte : CxType()

    @Serializable
    public data object Short : CxType()

    @Serializable
    public data object UShort : CxType()

    @Serializable
    public data object Int : CxType()

    @Serializable
    public data object UInt : CxType()

    @Serializable
    public data object Long : CxType()

    @Serializable
    public data object ULong : CxType()

    @Serializable
    public data object LongLong : CxType()

    @Serializable
    public data object ULongLong : CxType()

    @Serializable
    public data object Float : CxType()

    @Serializable
    public data object Double : CxType()

    //TODO: how to handle them???
    @Serializable
    public data object LongDouble : CxType()

    @Serializable
    public data object Int128 : CxType()

    @Serializable
    public data object UInt128 : CxType()

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
