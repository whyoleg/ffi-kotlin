package dev.whyoleg.foreign.gradle.api.interfaces

import org.gradle.api.provider.*

public interface JvmCxForeignInterfaceConfiguration : BaseCxForeignInterfaceConfiguration

public interface JvmPlatformCxForeignInterfaceConfiguration :
    JvmCxForeignInterfaceConfiguration,
    PlatformCxForeignInterfaceConfiguration {
    // JNI by default
    public val runtimeKind: Property<RuntimeKind>

    public fun macosArm64(configure: JvmTargetCxForeignInterfaceConfiguration.() -> Unit = {})
    public fun macosX64(configure: JvmTargetCxForeignInterfaceConfiguration.() -> Unit = {})
    public fun linuxX64(configure: JvmTargetCxForeignInterfaceConfiguration.() -> Unit = {})
    public fun mingwX64(configure: JvmTargetCxForeignInterfaceConfiguration.() -> Unit = {})

    public enum class RuntimeKind {
        JNI, FFM, MR_JAR,
        BOTH // TODO: both means both JNI and FFM will be in single jar and be used depending on JDK version - is it needed?
    }
}

public interface JvmTargetCxForeignInterfaceConfiguration :
    JvmCxForeignInterfaceConfiguration,
    TargetCxForeignInterfaceConfiguration {
    //embedDynamicLibraries
    //embedStaticLibraries
    //embedLinkPaths - TODO?
}
