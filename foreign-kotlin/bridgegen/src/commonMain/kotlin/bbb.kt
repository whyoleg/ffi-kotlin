package dev.whyoleg.foreign.bridgegen

import dev.whyoleg.foreign.clang.api.*

public fun buildCBridge(
    indexes: Map<String, CxIndex>
) {

}

public sealed class Relation {
    public class Shared(
        public val fragments: List<String>
    ) : Relation()
}
