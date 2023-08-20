package dev.whyoleg.foreign.cx.index.generator

import dev.whyoleg.foreign.cx.index.*

public expect fun CxIndex.Companion.generate(
    headers: Set<String>,
    includePaths: Set<String>
): CxIndex
