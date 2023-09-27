package dev.whyoleg.foreign.tooling.cx.compiler.clang

import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*

internal val CXFile.fileName: String get() = clang_getFileName(this).useString()!!
