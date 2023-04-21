package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*

public inline fun ForeignCLibrary(
    index: CxIndex,
    kotlinPackage: String,
    libraryName: String,
    builderAction: ForeignCLibrary.Builder.() -> Unit
): ForeignCLibrary {
    TODO()
}

//this is something that should be stored inside klib/jar and accessed by other libraries
// to be able to handle cross-references in dependent libraries
public class ForeignCLibrary private constructor() {
    public class Builder internal constructor() {
        public fun filter(block: CxIndex.Filter.() -> Unit) {

        }

        public fun visibility(selector: DeclarationSelector<CxDeclarationInfo, Visibility>) {

        }

        public fun subpackage(selector: DeclarationSelector<CxDeclarationInfo, String>) {

        }
    }
}
