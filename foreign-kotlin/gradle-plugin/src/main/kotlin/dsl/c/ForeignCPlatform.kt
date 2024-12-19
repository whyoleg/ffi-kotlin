package dev.whyoleg.foreign.gradle.dsl.c

import dev.whyoleg.foreign.gradle.dsl.*
import org.gradle.api.provider.*
import org.jetbrains.kotlin.gradle.plugin.*

public interface ForeignCPlatform {
    public val kotlinPlatformType: KotlinPlatformType
}

public interface ForeignJvmCPlatform : ForeignCPlatform {
    override val kotlinPlatformType: KotlinPlatformType get() = KotlinPlatformType.jvm

    // BOTH by default
    public val runtimeType: Property<ForeignJvmRuntimeType>

    public fun macosArm64(configure: ForeignCTarget.() -> Unit = {})
    public fun macosX64(configure: ForeignCTarget.() -> Unit = {})
    public fun linuxArm64(configure: ForeignCTarget.() -> Unit = {})
    public fun linuxX64(configure: ForeignCTarget.() -> Unit = {})
    public fun mingwX64(configure: ForeignCTarget.() -> Unit = {})
}

public interface ForeignAndroidJvmCPlatform : ForeignCPlatform {
    override val kotlinPlatformType: KotlinPlatformType get() = KotlinPlatformType.androidJvm

    public fun arm32(configure: ForeignCTarget.() -> Unit = {})
    public fun arm64(configure: ForeignCTarget.() -> Unit = {})
    public fun x64(configure: ForeignCTarget.() -> Unit = {})
    public fun x86(configure: ForeignCTarget.() -> Unit = {})
}

public interface ForeignNativeCPlatform : ForeignCPlatform {
    override val kotlinPlatformType: KotlinPlatformType get() = KotlinPlatformType.native

    public fun macosArm64(configure: ForeignCTarget.() -> Unit = {})
    public fun macosX64(configure: ForeignCTarget.() -> Unit = {})
    public fun linuxArm64(configure: ForeignCTarget.() -> Unit = {})
    public fun linuxX64(configure: ForeignCTarget.() -> Unit = {})
    public fun mingwX64(configure: ForeignCTarget.() -> Unit = {})
}

public interface ForeignWebCPlatform : ForeignCPlatform {
    public fun wasm(configure: ForeignCTarget.() -> Unit = {})
}

public interface ForeignJsCPlatform : ForeignWebCPlatform {
    override val kotlinPlatformType: KotlinPlatformType get() = KotlinPlatformType.js
}

public interface ForeignWasmJsCPlatform : ForeignWebCPlatform {
    override val kotlinPlatformType: KotlinPlatformType get() = KotlinPlatformType.wasm
}
