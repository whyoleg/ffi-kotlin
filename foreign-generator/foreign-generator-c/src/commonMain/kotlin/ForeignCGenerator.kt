package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*

public class ForeignCGenerator(
    private val index: CxIndex,
    private val kotlinPackage: String,
    private val libraryName: String,
    private val filter: CxIndex.Filter.() -> Unit
) {
    public fun generateCommon() {

    }

    public fun generateTarget(target: Target) {

    }
}

