package dev.whyoleg.foreign.codegen.kotlin

internal class KotlinCodegenConfiguration(
    val requiresOptIn: String?,
    val publicApi: Boolean,
    //val actuality: KotlinActuality,
) {
    val visibility = when (publicApi) {
        true -> KotlinVisibility.PUBLIC
        else -> KotlinVisibility.INTERNAL
    }
}
