package dev.whyoleg.foreign.gradle.dsl.c

import org.gradle.api.*
import org.gradle.api.provider.*
import org.jetbrains.kotlin.gradle.plugin.*

public interface ForeignCInterface : Named {
    // main by default
    public val sourceSetTree: Property<KotlinSourceSetTree>

    // false by default
    public val publicApi: Property<Boolean>

    // null by default
    public val requiresOptIn: Property<String?>

    public val foreignTargets: NamedDomainObjectContainer<ForeignCTarget>

    public fun jvm(configure: Action<ForeignJvmCPlatform>)
    public fun androidJvm(configure: Action<ForeignJvmCPlatform>)
    public fun native(configure: Action<ForeignNativeCPlatform>)
    public fun js(configure: Action<ForeignJsCPlatform>)
    public fun wasmJs(configure: Action<ForeignWasmJsCPlatform>)
}
