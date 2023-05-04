package dev.whyoleg.foreign.index.cx

import kotlinx.serialization.*
import kotlin.jvm.*

@Serializable
@JvmInline
public value class CxHeaderName(public val value: String) {
    public companion object {
        public val BuiltIn: CxHeaderName = CxHeaderName("_.h")
    }
}

@Serializable
public data class CxHeaderInfo(
    val name: CxHeaderName,
    val typedefs: List<CxTypedefInfo> = emptyList(),
    val records: List<CxRecordInfo> = emptyList(),
    val enums: List<CxEnumInfo> = emptyList(),
    val functions: List<CxFunctionInfo> = emptyList(),
) {
    public fun isEmpty(): Boolean = typedefs.isEmpty() && records.isEmpty() && enums.isEmpty() && functions.isEmpty()
    public fun isNotEmpty(): Boolean = !isEmpty()
}
