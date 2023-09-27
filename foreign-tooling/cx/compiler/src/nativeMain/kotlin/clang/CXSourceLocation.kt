package dev.whyoleg.foreign.tooling.cx.compiler.clang

import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import kotlinx.cinterop.*

internal val CValue<CXSourceLocation>.file: CXFile?
    get() = memScoped {
        val fileVar = alloc<CXFileVar>()
        clang_getFileLocation(this@file, fileVar.ptr, null, null, null)
        fileVar.value
    }
