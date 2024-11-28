package dev.whyoleg.foreign.gradle.dsl

import dev.whyoleg.foreign.gradle.dsl.c.*
import org.gradle.api.*

public interface ForeignExtension {
    public val interfaces: PolymorphicDomainObjectContainer<out ForeignInterface>
    public fun c(name: String, configure: ForeignCInterface.() -> Unit)
}
