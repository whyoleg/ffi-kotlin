package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*

public actual object CxCompiler {
    public actual fun buildIndex(
        mainFilePath: String,
        compilerArgs: List<String>
    ): CxCompilerIndex = useIndex { index ->
        useTranslationUnit(index, mainFilePath, compilerArgs) { translationUnit ->
            useIndexAction(index) { action ->
                indexTranslationUnit(action, translationUnit, cxCompilerIndexBuilder())
            }
        }
    }
}
