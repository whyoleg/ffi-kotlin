package dev.whyoleg.foreign.tool.cbridge

import dev.whyoleg.foreign.tool.cbridge.api.*

// input = macosArm64, linuxX64, ...
// output = native
internal fun commonize(
    name: CbFragmentName,
    fragments: List<CbFragment>
): CbFragment {
    if (fragments.size == 1) return fragments.single().copy(name = name)
}
