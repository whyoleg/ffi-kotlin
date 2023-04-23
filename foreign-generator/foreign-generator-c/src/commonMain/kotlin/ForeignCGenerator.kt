package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.schema.c.*

public class ForeignCGenerator(
    private val library: ForeignCLibrary
) {
    public fun generateCommon(): List<FileStub> = buildList {
        library.packages.forEach { pkg ->
            val path = pkg.name.replace(".", "/")
            val pkgRoot = pkg.name.substringAfterLast(".")
            if (pkg.typedefs.isNotEmpty()) add(
                kotlinFile(
                    path = "$path/typedefs.kt",
                    kotlinPackage = pkg.name,
                    imports = KotlinImports.Empty
                ) {
                    pkg.typedefs.joinTo(
                        this,
                        separator = "\n\n",
                        postfix = "\n"
                    ) { declaration ->
                        val typedef = library.index.typedef(declaration.id)
                        typedef.toKotlinDeclaration(library.index, declaration.visibility)
                    }
                }
            )
            if (pkg.functions.isNotEmpty()) add(
                kotlinFile(
                    path = "$path/functions.kt",
                    kotlinPackage = pkg.name,
                    imports = KotlinImports.Default
                ) {
                    pkg.functions.joinTo(
                        this,
                        separator = "\n\n",
                        postfix = "\n"
                    ) { declaration ->
                        val function = library.index.function(declaration.id)
                        function.toKotlinExpectDeclaration(library.index, declaration.visibility)
                    }
                }
            )
            pkg.structs.forEach { declaration ->
                val struct = library.index.struct(declaration.id)
                add(
                    kotlinFile(
                        path = "$path/${struct.name.value}.kt",
                        kotlinPackage = pkg.name,
                        imports = KotlinImports.Default
                    ) {
                        append(struct.toKotlinDeclaration(library.index))
                    }
                )
            }
        }
    }

    public fun generateTarget(target: Target) {

    }
}
