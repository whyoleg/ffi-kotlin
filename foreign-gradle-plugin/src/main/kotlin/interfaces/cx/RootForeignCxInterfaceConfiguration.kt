package dev.whyoleg.foreign.gradle.interfaces.cx

import dev.whyoleg.foreign.gradle.interfaces.*
import org.gradle.api.*
import org.gradle.api.provider.*
import org.jetbrains.kotlin.gradle.plugin.*

public sealed interface RootForeignCxInterfaceConfiguration : ForeignCxInterfaceConfiguration, ForeignInterfaceConfiguration {
    public val sourceSetTree: Property<KotlinSourceSetTree>

    public fun jvm(configure: JvmRootForeignCxInterfaceConfiguration.() -> Unit)
    public fun native(configure: NativeRootForeignCxInterfaceConfiguration.() -> Unit)
    public fun android(configure: AndroidRootForeignCxInterfaceConfiguration.() -> Unit)
    // TODO: add js, wasmJs, wasmWasi

    public override val bindings: Bindings

    public sealed interface Bindings : ForeignCxInterfaceConfiguration.Bindings {
        // false by default
        public val public: Property<Boolean>

        // null by default
        public val requiresOptIn: Property<String?>

        // by default will be {project.group}.{project.name.replace("-", ".")}.{interface.name}
        // for now, all of this is by header - could be improved later
        // header -> package name
        public fun packageName(transformer: Transformer<String, String>)
        public fun packageName(value: String): Unit = packageName { value }
    }
}
