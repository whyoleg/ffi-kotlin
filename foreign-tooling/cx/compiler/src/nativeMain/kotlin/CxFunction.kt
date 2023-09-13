package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*

internal fun CxIndexBuilder.buildFunction(
    id: CxDeclarationId,
    name: CxDeclarationName?,
    headerName: CxHeaderName?,
    cursor: CValue<CXCursor>,
): CxFunction = CxFunction(
    id = id,
    name = checkNotNull(name) { "function can not be unnamed" },
    headerName = headerName,
    returnType = buildType(clang_getCursorResultType(cursor)),
    parameters = buildList {
        repeat(clang_Cursor_getNumArguments(cursor)) { i ->
            val argCursor = clang_Cursor_getArgument(cursor, i.convert())
            add(
                CxFunction.Parameter(
                    name = argCursor.spelling.takeIf(String::isNotBlank) ?: "p$i",
                    type = buildType(argCursor.type)
                )
            )
        }
    }
)
