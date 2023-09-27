package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.clang.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*

public actual object CxCompiler {
    public actual fun buildIndex(
        mainFileName: String,
        mainFilePath: String,
        compilerArgs: List<String>
    ): CxCompilerIndex = useIndex { index ->
        useTranslationUnit(index, mainFilePath, compilerArgs) { translationUnit ->
            val parser = CxCompilerParser()
            parser.parseTranslationUnit(mainFileName, translationUnit)
            parser.getIndex()
        }
    }
}
