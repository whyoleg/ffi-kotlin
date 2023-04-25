package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.schema.c.*
import okio.*
import okio.Path.Companion.toPath

public fun main() {
    val buildPath = "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-generator/foreign-generator-c/build/foreign".toPath()

    val index = FileSystem.SYSTEM.readCxIndex(buildPath.resolve("libcrypto3.json"))

    val filteredIndex = index
        .filter {
            includeFunctions(recursive = true) { header, function ->
                header.name.value.startsWith("openssl/")
            }
        }
        .inlineTypedefs { header, typedef ->
            !header.name.value.startsWith("openssl/")
        }
        .excludeUnsupportedDeclarations()

    val library = ForeignCLibrary(
        name = "libcrypto3",
        rootPackage = "",
        index = filteredIndex,
    ) {
        visibility { header, declaration -> ForeignCDeclaration.Visibility.public }
        subpackage { header, _ ->
            header.name.value
                .substringBefore(".h")
                .substringBeforeLast("/")
                .replace("/", ".")
        }
    }

    FileSystem.SYSTEM.writeForeignCLibraryVerbose(buildPath.resolve("library"), library)

    val generatedPath = buildPath.resolve("generated")

    ForeignCGenerator(library).apply {
        FileSystem.SYSTEM.writeFileStubs(
            generatedPath.resolve("common/kotlin"),
            generateKotlinExpect()
        )
        FileSystem.SYSTEM.writeFileStubs(
            generatedPath.resolve("jni/kotlin"),
            generateKotlinJni(actual = true)
        )
        FileSystem.SYSTEM.writeFileStubs(
            generatedPath.resolve("jni/c"),
            generateCJni()
        )
        FileSystem.SYSTEM.writeFileStubs(
            generatedPath.resolve("emscripten/c"),
            generateCEmscripten()
        )
    }
}
