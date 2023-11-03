package dev.whyoleg.foreign.gradle.dsl.cxinterop

import org.gradle.api.*
import org.jetbrains.kotlin.gradle.plugin.*

public interface ForeignCxInteropContainer : NamedDomainObjectContainer<ForeignCxInterop> {
    public fun create(
        name: String,
        sourceSetTree: KotlinSourceSetTree,
        block: ForeignCxInterop.() -> Unit = {}
    ): ForeignCxInterop = create(name) {
        it.sourceSetTree.set(sourceSetTree)
        it.block()
    }

    public fun register(
        name: String,
        sourceSetTree: KotlinSourceSetTree,
        block: ForeignCxInterop.() -> Unit = {}
    ): NamedDomainObjectProvider<ForeignCxInterop> = register(name) {
        it.sourceSetTree.set(sourceSetTree)
        it.block()
    }
}
