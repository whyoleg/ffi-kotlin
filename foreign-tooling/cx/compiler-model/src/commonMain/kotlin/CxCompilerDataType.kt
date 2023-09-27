package dev.whyoleg.foreign.tooling.cx.compiler.model

import kotlinx.serialization.*

@Serializable // TODO: custom serializer?
public sealed class CxCompilerDataType {
    @Serializable
    public data class Primitive(val value: CxPrimitiveDataType) : CxCompilerDataType()

    @Serializable
    public data class Pointer(val pointed: CxCompilerDataType) : CxCompilerDataType()

    @Serializable
    public data class Enum(val id: CxCompilerDeclarationId) : CxCompilerDataType()

    @Serializable
    public data class Typedef(val id: CxCompilerDeclarationId) : CxCompilerDataType()

    @Serializable
    public sealed class Record : CxCompilerDataType() {
        @Serializable
        public data class Reference(val id: CxCompilerDeclarationId) : Record()

        @Serializable
        public data class Anonymous(val data: CxCompilerRecordData) : Record()
    }

    @Serializable
    public data class Function(val returnType: CxCompilerDataType, val parameters: List<CxCompilerDataType>) : CxCompilerDataType()

    @Serializable
    public sealed class Array : CxCompilerDataType() {
        public abstract val elementType: CxCompilerDataType
    }

    @Serializable
    public data class ConstArray(override val elementType: CxCompilerDataType, val size: Long) : Array()

    @Serializable
    public data class IncompleteArray(override val elementType: CxCompilerDataType) : Array()

    @Serializable
    public data class Unsupported(val info: String) : CxCompilerDataType()
}
