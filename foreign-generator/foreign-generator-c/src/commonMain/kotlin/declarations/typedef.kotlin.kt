package dev.whyoleg.foreign.generator.c.declarations

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.schema.c.*

internal fun CxTypedefInfo.toKotlinDeclaration(
    index: CxIndex,
    visibility: ForeignCDeclaration.Visibility
): String = buildString {
    append(visibility.name)
    append(" typealias ").append(name.value).append(" = ").append(aliased.type.toKotlinType(index))
}
