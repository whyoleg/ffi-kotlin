package dev.whyoleg.foreign.tooling.clang

import dev.whyoleg.foreign.tooling.clang.libclang.*
import kotlinx.cinterop.*

internal val CValue<CXCursor>.kind: CXCursorKind get() = useContents { kind }
internal val CXCursorKind.isInvalid: Boolean get() = clang_isInvalid(this) > 0u
internal val CValue<CXCursor>.spelling: String? get() = clang_getCursorSpelling(this).useString()?.takeIf(String::isNotBlank)
internal val CValue<CXCursor>.type: CValue<CXType> get() = clang_getCursorType(this)
internal val CValue<CXCursor>.debugString: String
    get() {
        return "'${clang_getCursorUSR(this).useString()}' '$spelling' ($kind) | ${type.debugString} | ${location.file?.fileName}"
    }

internal val CValue<CXCursor>.location: CValue<CXSourceLocation> get() = clang_getCursorLocation(this)
internal val CValue<CXCursor>.includedFile: CXFile
    get() {
        ensureKind(CXCursorKind.CXCursor_InclusionDirective)
        return clang_getIncludedFile(this)!!
    }
internal val CValue<CXCursor>.canonical: CValue<CXCursor> get() = clang_getCanonicalCursor(this)
internal val CValue<CXCursor>.definition: CValue<CXCursor> get() = clang_getCursorDefinition(this)
internal val CValue<CXCursor>.isDefinition: Boolean get() = clang_isCursorDefinition(this) > 0u
internal val CValue<CXCursor>.usr: String
    get() {
        val usr = clang_getCursorUSR(this).useString()
        checkNotNull(usr) { "USR = null" }
        check(usr.isNotBlank()) { "USR is blank" }
        return usr
    }
internal val CValue<CXCursor>.isAnonymous: Boolean get() = clang_Cursor_isAnonymous(this) > 0u

internal fun CValue<CXCursor>.ensureKind(vararg kinds: CXCursorKind) {
    val cursorKind = this.kind
    check(cursorKind in kinds) { "Wrong cursor kind, expected one of ${kinds.contentToString()}, found '$cursorKind'" }
}

internal fun CValue<CXCursor>.throwWrongKind(): Nothing {
    error("Wrong kind of cursor: $debugString")
}

internal fun CValue<CXCursor>.forEachArgument(block: (argCursor: CValue<CXCursor>) -> Unit) {
    ensureKind(CXCursorKind.CXCursor_FunctionDecl)
    repeat(clang_Cursor_getNumArguments(this)) { i ->
        block(clang_Cursor_getArgument(this, i.convert()))
    }
}

internal fun CValue<CXCursor>.visitChildren(
    failFast: Boolean = true,
    block: (childCursor: CValue<CXCursor>) -> Unit
) {
    val visitor = when {
        failFast -> CursorChildVisitor.FailFast(block)
        else     -> CursorChildVisitor.CollectErrors(block, verbose = false)
    }
    val visitorRef = StableRef.create(visitor)
    clang_visitChildren(this, staticCFunction { childCursor, _, clientData ->
        clientData!!.asStableRef<CursorChildVisitor>().get().run {
            // TODO: for some reason after break it will steal execute
            try {
                visit(childCursor)
                CXChildVisitResult.CXChildVisit_Continue
            } catch (cause: Throwable) {
                reportError(cause)
            }
        }
    }, visitorRef.asCPointer())
    visitorRef.dispose()
    visitor.checkErrors()
}

private abstract class CursorChildVisitor(
    val visit: (childCursor: CValue<CXCursor>) -> Unit
) {
    abstract fun reportError(cause: Throwable): CXChildVisitResult
    abstract fun checkErrors()

    class FailFast(visit: (childCursor: CValue<CXCursor>) -> Unit) : CursorChildVisitor(visit) {
        private var error: Throwable? = null
        override fun reportError(cause: Throwable): CXChildVisitResult {
            error = cause
            return CXChildVisitResult.CXChildVisit_Break
        }

        override fun checkErrors() {
            throw error ?: return
        }
    }

    class CollectErrors(
        visit: (childCursor: CValue<CXCursor>) -> Unit,
        private val verbose: Boolean
    ) : CursorChildVisitor(visit) {
        private val errors = mutableListOf<Throwable>()
        override fun reportError(cause: Throwable): CXChildVisitResult {
            errors += cause
            return CXChildVisitResult.CXChildVisit_Continue
        }

        override fun checkErrors() {
            if (errors.isEmpty()) return
            if (verbose) errors.forEach(Throwable::printStackTrace)

            error(errors.joinToString("\n", "Visiting children failed:\n") { "  ${it.message ?: "UNKNOWN"}" })
        }
    }
}
