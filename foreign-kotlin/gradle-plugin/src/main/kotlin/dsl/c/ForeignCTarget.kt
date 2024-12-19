package dev.whyoleg.foreign.gradle.dsl.c

import dev.whyoleg.foreign.gradle.dsl.*
import org.gradle.api.*
import org.jetbrains.kotlin.gradle.plugin.*

public interface ForeignCTarget : Named {
    public val kotlinPlatformType: KotlinPlatformType
    public val foreignTargetType: ForeignTargetType

    public val bindings: ForeignCBindings
    public fun bindings(actions: Action<ForeignCBindings>)

    public val libraries: ForeignCLibraries
    public fun libraries(actions: Action<ForeignCLibraries>)
}
