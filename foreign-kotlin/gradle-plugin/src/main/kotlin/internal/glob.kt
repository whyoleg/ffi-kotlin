package dev.whyoleg.foreign.gradle.plugin.internal

private val unsupportedCharacters = charArrayOf(
    '[', ']',
    '{', '}',
    '\\'
)

internal fun globToRegex(pattern: String): String {
    if (pattern.any { unsupportedCharacters.contains(it) }) {
        error("${unsupportedCharacters.contentToString()} are not supported yet")
    }

    return buildString(pattern.length) {
        pattern.forEach {
            when (it) {
                '*'  -> append(".*")
                '?'  -> append('.')
                // escaped
                '$', '%',
                '(', ')',
                '+', '.',
                '@', '^',
                '|'  -> append("\\$it")

                else -> append(it)
            }
        }
    }
}
