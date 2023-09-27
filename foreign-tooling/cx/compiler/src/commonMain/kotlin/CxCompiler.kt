package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.model.*

public expect object CxCompiler {
    public fun buildIndex(
        mainFileName: String,
        mainFilePath: String,
        compilerArgs: List<String>
    ): CxCompilerIndex
}
