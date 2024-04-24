package dev.whyoleg.foreign.gradle.plugin.dsl.cinterface

import dev.whyoleg.foreign.gradle.plugin.dsl.*
import org.gradle.api.*

public interface ForeignAndroidCInterface : ForeignPlatformCInterface, ForeignAndroidBaseCInterface {
    override val targets: NamedDomainObjectContainer<ForeignAndroidTargetCInterface>
    public fun arm32(configure: ForeignAndroidTargetCInterface.() -> Unit = {})
    public fun arm64(configure: ForeignAndroidTargetCInterface.() -> Unit = {})
    public fun x64(configure: ForeignAndroidTargetCInterface.() -> Unit = {})
    public fun x86(configure: ForeignAndroidTargetCInterface.() -> Unit = {})
}

public interface ForeignAndroidTargetCInterface : ForeignTargetCInterface, ForeignAndroidBaseCInterface {
    override val foreignTarget: ForeignTarget.Android
}

public interface ForeignAndroidBaseCInterface : ForeignBaseCInterface {
    //embeddedLibrariesPath=foreignLibs/*os* - should it be configurable?
    //embeddedLibrariesHashing=true
    //embedDynamicLibraries
    //embedStaticLibraries
    //embedLinkPaths - TODO?
}
