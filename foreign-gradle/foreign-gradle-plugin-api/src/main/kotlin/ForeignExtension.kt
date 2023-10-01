package dev.whyoleg.foreign.gradle.api

import dev.whyoleg.foreign.gradle.api.interfaces.*

public interface ForeignExtension {
    public val interfaces: ForeignInterfaces
    public fun interfaces(configure: ForeignInterfaces.() -> Unit): Unit = configure(interfaces)
}
