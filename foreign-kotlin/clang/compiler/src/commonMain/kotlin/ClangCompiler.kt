package dev.whyoleg.foreign.clang.compiler

import dev.whyoleg.foreign.clang.api.*

public object ClangCompiler {
    public fun buildIndex(
        headersPath: String,
        compilerArgs: List<String>
    ): CxIndex = useIndex { index ->
        val indexer = ClangIndexer()
        useTranslationUnit(index, headersPath, compilerArgs, indexer::indexTranslationUnit)
        indexer.buildIndex()
    }
}
