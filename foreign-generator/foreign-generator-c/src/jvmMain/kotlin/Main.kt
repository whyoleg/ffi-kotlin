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
        inlineTypedefs { header, typedef ->
            !header.name.value.startsWith("openssl/")
        }
        includeFunctions(recursive = true) { header, function ->
            when (header.name.value) {
                "openssl/err.h" -> function.name.value.startsWith("ERR")
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
            header.name.value.substringBefore(".h").replace("/", ".")
        }
    }

//    FileSystem.SYSTEM.writeForeignCLibraryVerbose(
//        "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-generator/foreign-generator-c/build/libcrypto3".toPath(),
//        library
//    )

    ForeignCGenerator(library).apply {
        FileSystem.SYSTEM.writeFileStubs(
            "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-generator/foreign-generator-c/build/files/common".toPath(),
            generateCommon()
        )
    }
}
