package dev.whyoleg.foreign.clang.compiler

import dev.whyoleg.foreign.clang.compiler.libclang.*

internal val CXFile.fileName: String get() = clang_getFileName(this).useString()!!
