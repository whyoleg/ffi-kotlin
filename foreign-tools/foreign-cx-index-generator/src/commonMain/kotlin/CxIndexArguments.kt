package dev.whyoleg.foreign.index.cx.generator

import dev.whyoleg.foreign.index.cx.*
import kotlinx.serialization.*

@Serializable
internal data class CxIndexArguments(
    val headers: Set<String>,
    val includePaths: Set<String>
)

@Serializable
internal data class CxIndexResult(
    val index: CxIndex?,
    val error: String?
)
