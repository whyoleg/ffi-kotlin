package dev.whyoleg.foreign.codegen.kotlin

internal enum class KotlinActuality(
    val value: String?
) {
    EXPECT("expect"),
    ACTUAL("actual"),
    NONE(null)
}