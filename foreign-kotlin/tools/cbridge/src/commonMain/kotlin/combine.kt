package dev.whyoleg.foreign.tool.cbridge

import dev.whyoleg.foreign.tool.cbridge.api.*

// for jvm and android targets
// may be for browser+nodejs (if it's needed/possible)
// input = jvm[macosArm64], jvm[linuxX64]
// output = jvm
internal fun combine(
    name: CbFragmentName,
    fragments: List<CbFragment>
): CbFragment {
    if (fragments.size == 1) return fragments.single().copy(name = name)
}
