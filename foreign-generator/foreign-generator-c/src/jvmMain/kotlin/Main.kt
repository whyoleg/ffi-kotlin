package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.schema.c.*
import okio.*
import okio.Path.Companion.toPath

public fun main() {
    val index = FileSystem.SYSTEM.readCxIndex(
        "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-cx-index/build/libcrypto3.json".toPath()
    )

    val filteredIndex = index.filter {
        excludeFunctionArguments()
        inlineTypedefs { header, typedef ->
            !header.name.value.startsWith("openssl/")
        }
        includeFunctions(recursive = true) { header, function ->
            when (header.name.value) {
                "openssl/evp.h" -> true
                else            -> false
            }
        }
    }

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

//    FileSystem.SYSTEM.writeForeignCLibraryVerbose(
//        "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-generator/foreign-generator-c/build/libcrypto3".toPath(),
//        library
//    )

    val filesPath = "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-generator/foreign-generator-c/build/files".toPath()

    ForeignCGenerator(library).apply {
        FileSystem.SYSTEM.writeFileStubs(
            filesPath.resolve("common/kotlin"),
            generateKotlinExpect()
        )
        FileSystem.SYSTEM.writeFileStubs(
            filesPath.resolve("jni/kotlin"),
            generateKotlinJni(actual = true)
        )
        FileSystem.SYSTEM.writeFileStubs(
            filesPath.resolve("jni/c"),
            generateCJni()
        )
    }
}
