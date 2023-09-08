package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.internal.*
import dev.whyoleg.foreign.tooling.cx.compiler.libclang.*
import kotlinx.cinterop.*
import kotlin.test.*

class PrimitivesTest {

    @Test
    fun test() {
        val collector = CxIndexPrimitiveCollector()
        useIndex { index ->
            //TODO: arguments
            //TODO: file from resources
            useTranslationUnit(
                index,
                "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-tools/foreign-cx-index-generator/src/nativeTest/resources/primitives.h",
                emptyList()
            ) { translationUnit ->
                useIndexHandler(collector) { handlerPointer, callbacksPointer ->
                    useIndexAction(index) { action ->
                        val result = clang_indexTranslationUnit(
                            action,
                            handlerPointer,
                            callbacksPointer,
                            sizeOf<IndexerCallbacks>().convert(),
                            0u,
                            translationUnit
                        )
                        if (result != 0) error("clang_indexTranslationUnit returned $result")
                    }
                }
            }
        }
        collector.types().forEach(::println)
    }
}
