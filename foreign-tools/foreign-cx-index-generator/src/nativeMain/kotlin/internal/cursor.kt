package dev.whyoleg.foreign.cx.index.generator.internal

import dev.whyoleg.foreign.cx.index.clang.*
import kotlinx.cinterop.*

internal val CValue<CXCursor>.spelling: String get() = clang_getCursorSpelling(this).useString()!!
internal val CValue<CXCursor>.type: CValue<CXType> get() = clang_getCursorType(this)
internal val CValue<CXCursor>.locationFile: CXFile?
    get() = memScoped {
        val fileVar = alloc<CXFileVar>()
        clang_getFileLocation(clang_getCursorLocation(this@locationFile), fileVar.ptr, null, null, null)
        fileVar.value
    }

internal fun CValue<CXString>.useString(): String? = try {
    clang_getCString(this)?.toKString()
} finally {
    clang_disposeString(this)
}

internal typealias CursorChildVisitor = (child: CValue<CXCursor>, parent: CValue<CXCursor>) -> CXChildVisitResult

internal fun visitChildren(cursor: CValue<CXCursor>, visitor: CursorChildVisitor) {
    val visitorRef = StableRef.create(visitor)
    try {
        clang_visitChildren(cursor, staticCFunction { child, parent, clientData ->
            clientData!!.asStableRef<CursorChildVisitor>().get().invoke(child, parent)
        }, visitorRef.asCPointer())
    } finally {
        visitorRef.dispose()
    }
}
