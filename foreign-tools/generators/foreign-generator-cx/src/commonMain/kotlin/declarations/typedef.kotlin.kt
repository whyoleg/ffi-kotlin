package dev.whyoleg.foreign.generator.cx.declarations

import dev.whyoleg.foreign.index.cx.*
import dev.whyoleg.foreign.schema.cx.*

internal fun CxTypedefInfo.toKotlinDeclaration(
    index: CxIndex,
    visibility: ForeignCDeclaration.Visibility
): String = buildString {
    append(visibility.name)
    append(" typealias ").append(name.value).append(" = ").append(aliased.type.toKotlinType(index))
}
