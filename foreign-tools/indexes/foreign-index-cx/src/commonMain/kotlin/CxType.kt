package dev.whyoleg.foreign.index.cx

import kotlinx.serialization.*

//TODO: decide on nullability of name and overall - is it needed, or we can just get name from type?
@Serializable
public data class CxTypeInfo(
    val name: String?,
    val type: CxType,
)

//TODO: custom serializer?
//TODO: use c names for types, dont mix with kotlin
@Serializable
public sealed class CxType {
    @Serializable
    public object Void : CxType()

    @Serializable
    public object Boolean : CxType()

    @Serializable
    public object Char : CxType()

    @Serializable
    public object Byte : CxType()

    @Serializable
    public object UByte : CxType()

    @Serializable
    public object Short : CxType()

    @Serializable
    public object UShort : CxType()

    @Serializable
    public object Int : CxType()

    @Serializable
    public object UInt : CxType()

    @Serializable
    public object Long : CxType()

    @Serializable
    public object ULong : CxType()

    @Serializable
    public object LongLong : CxType()

    @Serializable
    public object ULongLong : CxType()

    @Serializable
    public object Float : CxType()

    @Serializable
    public object Double : CxType()

    //TODO: how to handle them???
    @Serializable
    public object LongDouble : CxType()

    @Serializable
    public object Int128 : CxType()

    @Serializable
    public object UInt128 : CxType()

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
