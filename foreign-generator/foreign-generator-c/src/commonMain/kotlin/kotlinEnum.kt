package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.schema.c.*

internal fun CxEnumInfo.toKotlinDeclaration(
    visibility: ForeignCDeclaration.Visibility,
    name: CxDeclarationName
): String = buildString {
    append(visibility.name)
        .append(" enum class ").append(name.value)
        .append("(override val value: Value) : CEnum<").append(name.value).append(", ").append(name.value).append(".Value> {")
        .appendLine()

    constants.joinTo(
        this,
        separator = ",\n",
        postfix = ";\n\n"
    ) { constant ->
        "$INDENT${constant.name}(Value.${constant.name})"
    }

    append(INDENT)
        .append("@JvmInline public value class Value(override val underlying: Int) : CEnum.Value<").append(name.value).append(", Value> {")
        .appendLine()
    append(INDENT).append(INDENT)
        .append("public override fun toEnum(): ").append(name.value).append(" = get(underlying)")
        .appendLine()
    append(INDENT).append(INDENT)
        .append("public override fun toEnumOrNull(): ").append(name.value).append("? = getOrNull(underlying)")
        .appendLine()
    append(INDENT).append(INDENT)
        .append("override fun toString(): String = toString(underlying, \"").append(name.value).append("\")")
        .appendLine()
        .appendLine()

    append(INDENT).append(INDENT)
        .append("public companion object : CEnum.Cache<").append(name.value).append(", Value>(").append(name.value).append(".values()) {")
        .appendLine()


    constants.joinTo(
        this,
        separator = "\n",
        postfix = "\n"
    ) { constant ->
        "$INDENT$INDENT${INDENT}public val ${constant.name}: Value = Value(${constant.value})"
    }

    append(INDENT).append(INDENT)
        .append("}")
        .appendLine()
    append(INDENT)
        .append("}")
        .appendLine()

    append("}")
        .appendLine()
}
