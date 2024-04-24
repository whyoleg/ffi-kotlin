package dev.whyoleg.foreign.gradle.plugin.dsl.cinterface

import dev.whyoleg.foreign.gradle.plugin.dsl.*
import org.gradle.api.*
import org.gradle.api.provider.*

public interface ForeignJvmCInterface : ForeignPlatformCInterface, ForeignJvmBaseCInterface {
    // BOTH by default
    public val runtimeKind: Property<ForeignJvmRuntimeKind>

    override val targets: NamedDomainObjectContainer<out ForeignJvmTargetCInterface>
    public fun macosArm64(configure: ForeignJvmTargetCInterface.() -> Unit = {})
    public fun macosX64(configure: ForeignJvmTargetCInterface.() -> Unit = {})
    public fun linuxX64(configure: ForeignJvmTargetCInterface.() -> Unit = {})
    public fun mingwX64(configure: ForeignJvmTargetCInterface.() -> Unit = {})
}

public interface ForeignJvmTargetCInterface : ForeignTargetCInterface, ForeignJvmBaseCInterface {
    override val foreignTarget: ForeignTarget.Jvm
}

public interface ForeignJvmBaseCInterface : ForeignBaseCInterface {
    //embeddedLibrariesPath=foreignLibs/*os* - should it be configurable?
    //embeddedLibrariesHashing=true
    //embedDynamicLibraries
    //embedStaticLibraries
    //embedLinkPaths - TODO?
}
