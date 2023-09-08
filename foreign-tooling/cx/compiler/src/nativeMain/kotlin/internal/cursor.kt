package dev.whyoleg.foreign.tooling.cx.compiler.internal

import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import kotlinx.cinterop.*

internal val CValue<CXType>.spelling: String get() = clang_getTypeSpelling(this).useString()!!
internal val CValue<CXType>.kind: CXTypeKind get() = useContents { kind }

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

internal typealias CursorFieldVisitor = (fieldCursor: CValue<CXCursor>) -> CXVisitorResult

internal fun visitFields(type: CValue<CXType>, visitor: CursorFieldVisitor) {
    val visitorRef = StableRef.create(visitor)
    try {
        clang_Type_visitFields(type, staticCFunction { cursor, clientData ->
            clientData!!.asStableRef<CursorFieldVisitor>().get().invoke(cursor)
        }, visitorRef.asCPointer())
    } finally {
        visitorRef.dispose()
    }
}
