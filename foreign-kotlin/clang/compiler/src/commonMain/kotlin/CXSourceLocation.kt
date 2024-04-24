package dev.whyoleg.foreign.clang.compiler

import dev.whyoleg.foreign.clang.compiler.libclang.*
import kotlinx.cinterop.*

internal val CValue<CXSourceLocation>.file: CXFile?
    get() = memScoped {
        val fileVar = alloc<CXFileVar>()
        clang_getFileLocation(this@file, fileVar.ptr, null, null, null)
        fileVar.value
    }
