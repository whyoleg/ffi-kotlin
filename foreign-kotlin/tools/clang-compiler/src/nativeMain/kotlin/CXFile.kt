package dev.whyoleg.foreign.tool.clang.compiler

import dev.whyoleg.foreign.tool.clang.compiler.libclang.*

internal val CXFile.fileName: String get() = clang_getFileName(this).useString()!!
