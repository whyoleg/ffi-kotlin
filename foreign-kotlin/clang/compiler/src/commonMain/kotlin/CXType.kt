package dev.whyoleg.foreign.clang.compiler

import dev.whyoleg.foreign.clang.compiler.libclang.*
import kotlinx.cinterop.*

internal val CValue<CXType>.kind: CXTypeKind get() = useContents { kind }
internal val CValue<CXType>.spelling: String get() = clang_getTypeSpelling(this).useString()!!
internal val CValue<CXType>.cursor: CValue<CXCursor> get() = clang_getTypeDeclaration(this)

internal val CValue<CXType>.debugString: String get() = "'$spelling' ($kind)"
