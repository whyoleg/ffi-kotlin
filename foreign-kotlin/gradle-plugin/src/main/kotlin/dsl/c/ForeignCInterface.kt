package dev.whyoleg.foreign.gradle.dsl.c

import dev.whyoleg.foreign.gradle.dsl.*
import org.gradle.api.*
import org.gradle.api.provider.*
import org.jetbrains.kotlin.gradle.plugin.*

public interface ForeignCInterface : Named {
    // main by default
    public val sourceSetTree: Property<KotlinSourceSetTree>

    // false by default
    public val publicApi: Property<Boolean>

    // SOMETHING by default
    public val requiresOptIn: Property<String?>

//    // `foreign.{interface.name}` by default
//    public val mainPackageName: Property<String>
//
//    // `foreign.{interface.name}.support` by default
//    public val supportPackageName: Property<String>
//
//    // * by default - all in the main package
//    public val mainPackageHeadersRegex: Property<String>

    // true by default
    // will cause adding foreign-runtime-c to dependencies
    // if `publicApi` = true -> as `api` dependency
    // if `publicApi` = false -> as `implementation` dependency
    public val autoRuntimeDependencies: Property<Boolean>

    // BOTH by default
    public val jvmRuntimeKind: Property<ForeignJvmRuntimeKind>

    public val foreignTargets: NamedDomainObjectContainer<out ForeignCInterfaceTarget>

    public fun jvmMacosArm64(configure: ForeignJvmCInterfaceTarget.() -> Unit = {})
    public fun jvmMacosX64(configure: ForeignJvmCInterfaceTarget.() -> Unit = {})
    public fun jvmLinuxX64(configure: ForeignJvmCInterfaceTarget.() -> Unit = {})
    public fun jvmMingwX64(configure: ForeignJvmCInterfaceTarget.() -> Unit = {})

    public fun androidArm32(configure: ForeignAndroidCInterfaceTarget.() -> Unit = {})
    public fun androidArm64(configure: ForeignAndroidCInterfaceTarget.() -> Unit = {})
    public fun androidX64(configure: ForeignAndroidCInterfaceTarget.() -> Unit = {})
    public fun androidX86(configure: ForeignAndroidCInterfaceTarget.() -> Unit = {})

    public fun nativeMacosArm64(configure: ForeignNativeCInterfaceTarget.() -> Unit = {})
    public fun nativeMacosX64(configure: ForeignNativeCInterfaceTarget.() -> Unit = {})
    public fun nativeLinuxX64(configure: ForeignNativeCInterfaceTarget.() -> Unit = {})
    public fun nativeMingwX64(configure: ForeignNativeCInterfaceTarget.() -> Unit = {})

    public fun js(configure: ForeignWasmCInterfaceTarget.() -> Unit = {}) {}
    public fun wasmJs(configure: ForeignWasmCInterfaceTarget.() -> Unit = {}) {}
}
