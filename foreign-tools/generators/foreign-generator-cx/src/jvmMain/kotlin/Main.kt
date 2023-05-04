package dev.whyoleg.foreign.generator.cx

import dev.whyoleg.foreign.index.cx.*
import dev.whyoleg.foreign.schema.cx.*
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

    FileSystem.SYSTEM.deleteRecursively(generatedPath)

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
            generatedPath.resolve("ffm/kotlin"),
            generateKotlinFfm(actual = true)
        )
        FileSystem.SYSTEM.writeFileStubs(
            generatedPath.resolve("native/kotlin"),
            generateKotlinNative(actual = true)
        )

        FileSystem.SYSTEM.writeFileStubs(
            generatedPath.resolve("jni/c"),
            generateCJni()
        )
        FileSystem.SYSTEM.writeFileStubs(
            generatedPath.resolve("native/c"),
            generateCNative()
        )
//        FileSystem.SYSTEM.writeFileStubs(
//            generatedPath.resolve("emscripten/c"),
//            generateCEmscripten()
//        )
    }
}
