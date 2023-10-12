package dev.whyoleg.foreign.gradle.api.interfaces

import org.gradle.api.*
import org.gradle.api.provider.*
import org.jetbrains.kotlin.gradle.plugin.*

// TODO: move to interfaces.cx package? or just cx?
// TODO: decide on name better?
public interface RootCxForeignInterfaceConfiguration : BaseCxForeignInterfaceConfiguration, ForeignInterfaceConfiguration {
    // main by default
    public val sourceSetTree: Property<KotlinSourceSetTree>

    public fun jvm(configure: JvmPlatformCxForeignInterfaceConfiguration.() -> Unit)
    public fun native(configure: NativePlatformCxForeignInterfaceConfiguration.() -> Unit)
    public fun android(configure: AndroidPlatformCxForeignInterfaceConfiguration.() -> Unit)
    // TODO: add js, wasmJs, wasmWasi

    // TODO: link to bridge and source generation tasks

    public override val bindings: Bindings

    public interface Bindings : BaseCxForeignInterfaceConfiguration.Bindings {
        // false by default
        public val public: Property<Boolean>

        // null by default
        public val requiresOptIn: Property<String?>

        // by default will be {project.group}.{project.name.replace("-", ".")}.{interface.name}
        // for now, all of this is by header - could be improved later
        // header -> package name
        // single value - will override previously set value
        public fun packageName(transformer: Transformer<String, String>)
        public fun packageName(value: String): Unit = packageName { value }
    }
}
