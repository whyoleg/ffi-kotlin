package dev.whyoleg.ffi.c.index

public sealed class CType {
    public object Void : CType()

    public object Boolean : CType()
    public object Char : CType()
    public object Byte : CType()
    public object UByte : CType()
    public object Short : CType()
    public object UShort : CType()
    public object Int : CType()
    public object UInt : CType()
    public object Long : CType()
    public object ULong : CType()
    public object LongLong : CType()
    public object ULongLong : CType()
    public object Float : CType()
    public object Double : CType()

    //how to handle them???
    public object LongDouble : CType()
    public object Int128 : CType()
    public object UInt128 : CType()

    public data class Pointer internal constructor(val pointed: CType) : CType()

    public data class Enum internal constructor(val usr: USR) : CType()
    public data class Typedef internal constructor(val usr: USR) : CType()
    public data class Struct internal constructor(val usr: USR) : CType()

    public data class Function internal constructor(val returnType: CType, val parameters: List<CType>) : CType()

    public data class ConstArray internal constructor(val elementType: CType, val size: kotlin.Long) : CType()
    public data class IncompleteArray internal constructor(val elementType: CType) : CType()

    public object Unknown : CType()
}
