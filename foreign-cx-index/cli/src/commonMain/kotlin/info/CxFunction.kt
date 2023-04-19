package dev.whyoleg.foreign.cx.index.cli.info

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.cx.index.clang.*
import dev.whyoleg.foreign.cx.index.cli.*
import dev.whyoleg.foreign.cx.index.cli.internal.*
import kotlinx.cinterop.*

fun CxIndexBuilder.buildFunctionInfo(
    id: CxDeclarationId,
    name: CxDeclarationName,
    cursor: CValue<CXCursor>,
): CxFunctionInfo = CxFunctionInfo(
    id = id,
    name = name,
    returnType = buildTypeInfo(clang_getCursorResultType(cursor)),
    parameters = buildList {
        repeat(clang_Cursor_getNumArguments(cursor)) { i ->
            val argCursor = clang_Cursor_getArgument(cursor, i.convert())
            add(
                CxFunctionInfo.Parameter(
                    name = argCursor.spelling,
                    type = buildTypeInfo(argCursor.type)
                )
            )
        }
    }
)
