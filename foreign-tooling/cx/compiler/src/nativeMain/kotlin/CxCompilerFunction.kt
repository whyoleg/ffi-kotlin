package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*

internal fun CxCompilerIndexBuilder.buildFunction(
    id: CxCompilerDeclarationId,
    declarationName: String?,
    headerName: String?,
    cursor: CValue<CXCursor>,
): CxCompilerFunction = CxCompilerFunction(
    id = id,
    declarationName = checkNotNull(declarationName) { "function can not be unnamed" },
    headerName = headerName,
    returnType = buildType(clang_getCursorResultType(cursor)),
    parameters = buildList {
        repeat(clang_Cursor_getNumArguments(cursor)) { i ->
            val argCursor = clang_Cursor_getArgument(cursor, i.convert())
            add(
                CxCompilerFunction.Parameter(
                    name = argCursor.spelling.takeIf(String::isNotBlank) ?: "p$i",
                    type = buildType(argCursor.type)
                )
            )
        }
    }
)
