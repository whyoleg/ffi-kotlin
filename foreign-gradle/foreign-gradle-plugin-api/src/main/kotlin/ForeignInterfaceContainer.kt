package dev.whyoleg.foreign.gradle.api

import dev.whyoleg.foreign.gradle.api.cx.*
import org.gradle.api.*

public interface ForeignInterfaceContainer : PolymorphicDomainObjectContainer<ForeignInterface> {
    public fun cx(
        name: String,
        configure: CxForeignInterface.() -> Unit = {}
    ): CxForeignInterface
}
