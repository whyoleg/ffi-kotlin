package dev.whyoleg.foreign.gradle.plugin.dsl.cinterface

import dev.whyoleg.foreign.gradle.plugin.dsl.*
import org.gradle.api.*

public interface ForeignNativeCInterface : ForeignPlatformCInterface, ForeignNativeBaseCInterface {
    override val targets: NamedDomainObjectContainer<ForeignNativeTargetCInterface>
    public fun macosArm64(configure: ForeignNativeTargetCInterface.() -> Unit = {})
    public fun macosX64(configure: ForeignNativeTargetCInterface.() -> Unit = {})
    public fun linuxX64(configure: ForeignNativeTargetCInterface.() -> Unit = {})
    public fun mingwX64(configure: ForeignNativeTargetCInterface.() -> Unit = {})

    public fun iosDeviceArm64(configure: ForeignNativeTargetCInterface.() -> Unit = {})
    public fun iosSimulatorArm64(configure: ForeignNativeTargetCInterface.() -> Unit = {})
    public fun iosSimulatorX64(configure: ForeignNativeTargetCInterface.() -> Unit = {})

    public fun androidArm32(configure: ForeignNativeTargetCInterface.() -> Unit = {})
    public fun androidArm64(configure: ForeignNativeTargetCInterface.() -> Unit = {})
    public fun androidX64(configure: ForeignNativeTargetCInterface.() -> Unit = {})
    public fun androidX86(configure: ForeignNativeTargetCInterface.() -> Unit = {})
}

public interface ForeignNativeTargetCInterface : ForeignTargetCInterface, ForeignNativeBaseCInterface {
    override val foreignTarget: ForeignTarget.Native
}

public interface ForeignNativeBaseCInterface : ForeignBaseCInterface {
    //embedStaticLibraries
    //embedLinkPaths
}
