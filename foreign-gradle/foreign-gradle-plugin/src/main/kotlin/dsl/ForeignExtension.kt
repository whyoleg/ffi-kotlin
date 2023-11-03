package dev.whyoleg.foreign.gradle.dsl

import dev.whyoleg.foreign.gradle.dsl.cxinterop.*

public interface ForeignExtension {
    public val cxInterops: ForeignCxInteropContainer
    public fun cxInterops(configure: ForeignCxInteropContainer.() -> Unit): Unit = configure(cxInterops)
}
