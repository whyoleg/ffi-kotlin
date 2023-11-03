package dev.whyoleg.foreign.gradle.interfaces

import dev.whyoleg.foreign.gradle.*
import dev.whyoleg.foreign.gradle.api.cx.*

internal abstract class DefaultRootForeignCxInterfaceConfigurationBindings :
    DefaultForeignCxInterfaceConfigurationBindings(),
    CxForeignInterface.Bindings {

    init {
        run {
            public.convention(false)
            requiresOptIn.convention(null as String?)
            //packageName.convention { defaultPackageName }
        }
    }
}

internal abstract class DefaultForeignCxInterfaceConfigurationBindings : BaseCxForeignInterfaceConfiguration.Bindings {
    fun addFrom(parent: DefaultForeignCxInterfaceConfigurationBindings) {
        initialHeaders.withAllFrom(parent.initialHeaders)
        includeHeaders.withAllFrom(parent.includeHeaders)
        excludeHeaders.withAllFrom(parent.excludeHeaders)
    }
}
