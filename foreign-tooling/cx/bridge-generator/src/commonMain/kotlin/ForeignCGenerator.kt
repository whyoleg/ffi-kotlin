package dev.whyoleg.foreign.cx.bindings.generator

import dev.whyoleg.foreign.cx.bindings.generator.declarations.*
import dev.whyoleg.foreign.cx.metadata.*

public class ForeignCGenerator(
    private val library: ForeignCLibrary
) {
    public fun generateKotlinExpect(): List<FileStub> = buildList {
        library.packages.forEach packageIterating@{ pkg ->
            val path = pkg.name.replace(".", "/")
            pkg.records.forEach { declaration ->
                val struct = library.index.record(declaration.id)
                val name = struct.name ?: return@forEach // anonymous structs/unions
                add(
                    kotlinFile(
                        path = "$path/${name.value}.kt",
                        kotlinPackage = pkg.name,
                        imports = KotlinImports.Struct
                    ) {
                        append(struct.toKotlinDeclaration(library.index, declaration.visibility, name))
                    }
                )
            }
            pkg.enums.forEach { declaration ->
                val enum = library.index.enum(declaration.id)
                val name = enum.name ?: return@forEach // anonymous structs/unions
                add(
                    kotlinFile(
                        path = "$path/${name.value}.kt",
                        kotlinPackage = pkg.name,
                        imports = KotlinImports.Enum
                    ) {
                        append(enum.toKotlinDeclaration(declaration.visibility, name))
                    }
                )
            }
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
                        function.toKotlinExpectDeclaration(library.index, library.name, declaration.visibility)
                    }
                }
            )
        }
    }

    public fun generateKotlinJni(actual: Boolean): List<FileStub> = buildList {
        library.packages.forEach { pkg ->
            val path = pkg.name.replace(".", "/")
            if (pkg.functions.isNotEmpty()) add(
                kotlinFile(
                    path = "$path/functions.jni.kt",
                    kotlinPackage = pkg.name,
                    imports = KotlinImports.Default
                ) {
                    pkg.functions.joinTo(
                        this,
                        separator = "\n\n",
                        postfix = "\n"
                    ) { declaration ->
                        val function = library.index.function(declaration.id)
                        function.toKotlinJniDeclaration(library.index, library.name, actual, declaration.visibility)
                    }
                }
            )
        }
    }

    public fun generateKotlinFfm(actual: Boolean): List<FileStub> = buildList {
        library.packages.forEach { pkg ->
            val path = pkg.name.replace(".", "/")
            if (pkg.functions.isNotEmpty()) add(
                kotlinFile(
                    path = "$path/functions.ffm.kt",
                    kotlinPackage = pkg.name,
                    imports = KotlinImports.DefaultFFM
                ) {
                    pkg.functions.joinTo(
                        this,
                        separator = "\n\n",
                        postfix = "\n"
                    ) { declaration ->
                        val function = library.index.function(declaration.id)
                        function.toKotlinFfmDeclaration(library.index, library.name, actual, declaration.visibility)
                    }
                }
            )
        }
    }

    public fun generateCJni(): List<FileStub> = buildList {
        //TODO?
        val includes = library.index.headers.map { it.name.value }
        library.packages.forEach { pkg ->
            val path = pkg.name.replace(".", "/")
            if (pkg.functions.isNotEmpty()) add(
                cFile(
                    path = "$path/functions.jni.c",
                    includes = includes
                ) {
                    pkg.functions.joinTo(
                        this,
                        separator = "\n\n",
                        postfix = "\n"
                    ) { declaration ->
                        val function = library.index.function(declaration.id)
                        function.toCJniDeclaration(library.index, pkg.name, "Functions_jniKt")
                    }
                }
            )
        }
    }

    public fun generateCEmscripten(): List<FileStub> = buildList {
        //TODO?
        val includes = library.index.headers.map { it.name.value }
        library.packages.forEach { pkg ->
            val path = pkg.name.replace(".", "/")
            if (pkg.functions.isNotEmpty()) add(
                cFile(
                    path = "$path/functions.emscripten.c",
                    includes = includes
                ) {
                    pkg.functions.joinTo(
                        this,
                        separator = "\n\n",
                        postfix = "\n"
                    ) { declaration ->
                        val function = library.index.function(declaration.id)
                        function.toCEmscriptenDeclaration(library.index)
                    }
                }
            )
        }
    }

    public fun generateKotlinNative(actual: Boolean): List<FileStub> = buildList {
        library.packages.forEach { pkg ->
            val path = pkg.name.replace(".", "/")
            if (pkg.functions.isNotEmpty()) add(
                kotlinFile(
                    path = "$path/functions.native.kt",
                    kotlinPackage = pkg.name,
                    imports = KotlinImports.Default
                ) {
                    pkg.functions.joinTo(
                        this,
                        separator = "\n\n",
                        postfix = "\n"
                    ) { declaration ->
                        val function = library.index.function(declaration.id)
                        function.toKotlinNativeDeclaration(library.index, library.name, actual, declaration.visibility)
                    }
                }
            )
        }
    }

    public fun generateCNative(): List<FileStub> = buildList {
        //TODO?
        val includes = library.index.headers.map { it.name.value }
        library.packages.forEach { pkg ->
            val path = pkg.name.replace(".", "/")
            if (pkg.functions.isNotEmpty()) add(
                cFile(
                    path = "$path/functions.native.c",
                    includes = includes
                ) {
                    pkg.functions.joinTo(
                        this,
                        separator = "\n\n",
                        postfix = "\n"
                    ) { declaration ->
                        val function = library.index.function(declaration.id)
                        function.toCNativeDeclaration(library.index)
                    }
                }
            )
        }
    }

    //generate JVM Library + JVM resources
    //generate Android.mk file

    //TODO: add WASM/JS later
    //generateKotlinEmscripten

    //generate Wasm Library
}
