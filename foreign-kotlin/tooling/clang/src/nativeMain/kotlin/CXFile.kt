package dev.whyoleg.foreign.tooling.clang

import dev.whyoleg.foreign.tooling.clang.libclang.*

internal val CXFile.fileName: String get() = clang_getFileName(this).useString()!!
