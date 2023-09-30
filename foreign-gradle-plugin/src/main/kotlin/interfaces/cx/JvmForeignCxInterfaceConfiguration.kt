package dev.whyoleg.foreign.gradle.interfaces.cx

import org.gradle.api.provider.*

public sealed interface JvmForeignCxInterfaceConfiguration : ForeignCxInterfaceConfiguration

public sealed interface JvmRootForeignCxInterfaceConfiguration : JvmForeignCxInterfaceConfiguration {
    public val runtimeKind: Property<RuntimeKind>

    public fun macosArm64(configure: JvmHostForeignCxInterfaceConfiguration.() -> Unit = {})
    public fun macosX64(configure: JvmHostForeignCxInterfaceConfiguration.() -> Unit = {})
    public fun linuxX64(configure: JvmHostForeignCxInterfaceConfiguration.() -> Unit = {})
    public fun mingwX64(configure: JvmHostForeignCxInterfaceConfiguration.() -> Unit = {})

    public enum class RuntimeKind {
        JNI, FFM, MR_JAR,
        BOTH // TODO: both means both JNI and FFM will be in single jar and be used depending on JDK version - is it needed?
    }
}

public sealed interface JvmHostForeignCxInterfaceConfiguration : JvmForeignCxInterfaceConfiguration {
    //embedDynamicLibraries
    //embedStaticLibraries
    //embedLinkPaths - TODO?
}
