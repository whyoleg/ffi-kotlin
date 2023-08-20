package dev.whyoleg.foreign.cx.index.generator

import dev.whyoleg.foreign.cx.index.*
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
