package dev.whyoleg.foreign.gradle.interfaces

import dev.whyoleg.foreign.gradle.*
import dev.whyoleg.foreign.gradle.api.interfaces.*
import org.gradle.api.*
import org.gradle.api.model.*
import org.gradle.api.provider.*
import org.gradle.api.specs.*
import org.gradle.kotlin.dsl.*

internal abstract class DefaultRootForeignCxInterfaceConfigurationBindings :
    DefaultForeignCxInterfaceConfigurationBindings(),
    RootCxForeignInterfaceConfiguration.Bindings {

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
