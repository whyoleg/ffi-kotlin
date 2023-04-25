package dev.whyoleg.foreign.generator.c


internal fun <T> List<T>.joinToIfNotEmpty(
    builder: StringBuilder,
    separator: CharSequence,
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    transform: (T) -> CharSequence
): StringBuilder {
    if (isNotEmpty()) joinTo(builder, separator, prefix, postfix, transform = transform)
    return builder
}
