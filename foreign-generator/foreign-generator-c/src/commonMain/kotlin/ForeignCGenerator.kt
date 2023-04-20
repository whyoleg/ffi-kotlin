package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*

public class ForeignCGenerator(
    private val index: CxIndex,
    private val kotlinPackage: String,
    private val libraryName: String,
    private val visibility: Visibility,
    filter: CxIndex.Filter.() -> Unit
) {
    private val filteredIndex = index.filter(filter)

    public fun generateCommon(): List<FileStub> = buildList {
        filteredIndex.headers.forEach { header ->
            if (header.typedefs.isNotEmpty()) add(
                kotlinFile(
                    path = header.name.value.replace(".h", ".typedefs.kt"),
                    kotlinPackage = kotlinPackage,
                    imports = KotlinImports.Empty
                ) {
                    header.typedefs.joinTo(
                        this,
                        separator = "\n\n",
                        postfix = "\n"
                    ) { typedef ->
                        typedef.toKotlinDeclaration(index, visibility)
                    }
                }
            )
            if (header.functions.isNotEmpty()) add(
                kotlinFile(
                    path = header.name.value.replace(".h", ".functions.kt"),
                    kotlinPackage = kotlinPackage,
                    imports = KotlinImports.Default
                ) {
                    header.functions.joinTo(
                        this,
                        separator = "\n\n",
                        postfix = "\n"
                    ) { function ->
                        function.toKotlinExpectDeclaration(index, visibility)
                    }
                }
            )
        }
    }

    public fun generateTarget(target: Target) {

    }
}
