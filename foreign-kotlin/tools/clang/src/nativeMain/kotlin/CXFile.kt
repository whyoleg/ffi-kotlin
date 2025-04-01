package dev.whyoleg.foreign.tool.clang

import dev.whyoleg.foreign.tool.libclang.*

internal val CXFile.fileName: String get() = clang_getFileName(this).useString()!!
