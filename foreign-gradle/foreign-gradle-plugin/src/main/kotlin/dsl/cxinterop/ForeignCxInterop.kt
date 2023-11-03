package dev.whyoleg.foreign.gradle.dsl.cxinterop

import org.gradle.api.*
import org.gradle.api.provider.*
import org.jetbrains.kotlin.gradle.plugin.*

public interface ForeignCxInterop : BaseCxInterop, Named {
    public val sourceSetTree: Property<KotlinSourceSetTree>

    // false by default
    public val publicApi: Property<Boolean>

    // null by default
    public val requiresOptIn: Property<String?>

    public fun jvm(configure: JvmPlatformCxInterop.() -> Unit)
    public fun native(configure: NativePlatformCxInterop.() -> Unit)
    public fun android(configure: AndroidPlatformCxInterop.() -> Unit)
}

