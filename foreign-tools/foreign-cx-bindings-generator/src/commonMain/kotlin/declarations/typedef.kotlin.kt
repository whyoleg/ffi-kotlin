package dev.whyoleg.foreign.cx.bindings.generator.declarations

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.cx.metadata.*

internal fun CxTypedefInfo.toKotlinDeclaration(
    index: CxIndex,
    visibility: ForeignCDeclaration.Visibility
): String = buildString {
    append(visibility.name)
    append(" typealias ").append(name.value).append(" = ").append(aliased.type.toKotlinType(index))
}
