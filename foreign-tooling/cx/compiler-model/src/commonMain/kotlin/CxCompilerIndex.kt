package dev.whyoleg.foreign.tooling.cx.compiler.model

import kotlinx.serialization.*

// todo: redesign declarations - no need to have maps?
@Serializable
public data class CxCompilerIndex(
    val variables: List<CxCompilerVariable>,
    val enums: List<CxCompilerEnum>,
    val records: List<CxCompilerRecord>,
    val typedefs: List<CxCompilerTypedef>,
    val functions: List<CxCompilerFunction>,
)
