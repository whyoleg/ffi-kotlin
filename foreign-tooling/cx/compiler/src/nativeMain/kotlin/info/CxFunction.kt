package dev.whyoleg.foreign.tooling.cx.compiler.info

import dev.whyoleg.foreign.tooling.cx.compiler.*
import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlinx.cinterop.*

internal fun CxIndexBuilder.buildFunctionInfo(
    id: CxDeclarationId,
    name: CxDeclarationName?,
    cursor: CValue<CXCursor>,
): CxFunctionInfo = CxFunctionInfo(
    id = id,
    name = checkNotNull(name) { "function can not be unnamed" },
    returnType = buildTypeInfo(clang_getCursorResultType(cursor)),
    parameters = buildList {
        repeat(clang_Cursor_getNumArguments(cursor)) { i ->
            val argCursor = clang_Cursor_getArgument(cursor, i.convert())
            add(
                CxFunctionInfo.Parameter(
                    name = argCursor.spelling.takeIf(String::isNotBlank) ?: "p$i",
                    type = buildTypeInfo(argCursor.type)
                )
            )
        }
    }
)
