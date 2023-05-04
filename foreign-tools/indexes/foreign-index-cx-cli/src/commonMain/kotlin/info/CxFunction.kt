package dev.whyoleg.foreign.index.cx.cli.info

import dev.whyoleg.foreign.index.cx.*
import dev.whyoleg.foreign.index.cx.clang.*
import dev.whyoleg.foreign.index.cx.cli.*
import dev.whyoleg.foreign.index.cx.cli.internal.*
import kotlinx.cinterop.*

fun CxIndexBuilder.buildFunctionInfo(
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
