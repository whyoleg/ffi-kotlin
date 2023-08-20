package dev.whyoleg.foreign.index.cx.generator

import dev.whyoleg.foreign.index.cx.*

public expect fun CxIndex.Companion.generate(
    headers: Set<String>,
    includePaths: Set<String>
): CxIndex
