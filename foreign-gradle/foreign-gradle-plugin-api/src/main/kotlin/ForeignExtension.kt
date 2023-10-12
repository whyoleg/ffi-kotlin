package dev.whyoleg.foreign.gradle.api

import dev.whyoleg.foreign.gradle.api.interfaces.*

// TODO decide on public packages
//  maybe change package:
//  from dev.whyoleg.foreign.gradle.api
//  to   dev.whyoleg.foreign.gradle.plugin
public interface ForeignExtension {
    public val interfaces: ForeignInterfaces
    public fun interfaces(configure: ForeignInterfaces.() -> Unit): Unit = configure(interfaces)
}
