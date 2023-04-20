package dev.whyoleg.foreign.cx.index

import kotlinx.serialization.*
import kotlin.jvm.*

@Serializable
@JvmInline
public value class CxHeaderName(public val value: String) {
    public companion object {
        public val BuiltIn: CxHeaderName = CxHeaderName("_")
    }
}

@Serializable
public data class CxHeaderInfo(
    val name: CxHeaderName,
    val typedefs: List<CxTypedefInfo> = emptyList(),
    val structs: List<CxStructInfo> = emptyList(),
    val enums: List<CxEnumInfo> = emptyList(),
    val functions: List<CxFunctionInfo> = emptyList(),
) {
    public fun isEmpty(): Boolean = typedefs.isEmpty() && structs.isEmpty() && enums.isEmpty() && functions.isEmpty()
    public fun isNotEmpty(): Boolean = !isEmpty()
}
