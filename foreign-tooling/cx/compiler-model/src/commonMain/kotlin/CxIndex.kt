package dev.whyoleg.foreign.tooling.cx.compiler.model

import kotlinx.serialization.*

@Serializable
public data class CxIndex(
    val typedefs: List<CxTypedef>,
    val enums: List<CxEnum>,
    val records: List<CxRecord>,
    val functions: List<CxFunction>,
)
