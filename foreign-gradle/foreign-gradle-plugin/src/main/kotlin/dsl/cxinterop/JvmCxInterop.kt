package dev.whyoleg.foreign.gradle.dsl.cxinterop

import dev.whyoleg.foreign.gradle.tooling.*
import org.gradle.api.provider.*

public interface JvmBaseCxInterop : BaseCxInterop

public interface JvmPlatformCxInterop : JvmBaseCxInterop, PlatformCxInterop {
    // JNI by default
    public val runtimeKind: Property<JvmRuntimeKind>

    public fun macosArm64(configure: JvmTargetCxInterop.() -> Unit = {})
    public fun macosX64(configure: JvmTargetCxInterop.() -> Unit = {})
    public fun linuxX64(configure: JvmTargetCxInterop.() -> Unit = {})
    public fun mingwX64(configure: JvmTargetCxInterop.() -> Unit = {})
}

public interface JvmTargetCxInterop : JvmBaseCxInterop, TargetCxInterop {
    //embeddedLibrariesPath=foreignLibs/*os* - should it be configurable?
    //embeddedLibrariesHashing=true
    //embedDynamicLibraries
    //embedStaticLibraries
    //embedLinkPaths - TODO?
}
