package dev.whyoleg.foreign.gradle.internal.cx

import dev.whyoleg.foreign.gradle.dsl.cxinterop.*
import org.gradle.api.*

internal class DefaultForeignCxInteropContainer(
    delegate: NamedDomainObjectContainer<ForeignCxInterop>
) : ForeignCxInteropContainer, NamedDomainObjectContainer<ForeignCxInterop> by delegate {
}
