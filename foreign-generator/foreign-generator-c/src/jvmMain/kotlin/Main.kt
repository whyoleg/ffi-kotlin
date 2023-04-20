package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*
import okio.*
import okio.Path.Companion.toPath

public fun main() {
    val index = FileSystem.SYSTEM.readCxIndex(
        "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-cx-index/build/index.json".toPath()
    )

    index.filter {
//        includeHeaders { it.name.value.startsWith("openssl/") }
        includeFunctions(recursive = true) {
            when (it.name.value) {
                "EVP_DigestSignInit_ex" -> true
                else                    -> false
            }
        }
    }.also {
        FileSystem.SYSTEM.writeCxIndexVerbose(
            "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-generator/foreign-generator-c/build/index.json".toPath(),
            it
        )
    }
    return

    val generator = ForeignCGenerator(
        index = index,
        kotlinPackage = "dev.whyoleg.ffi.libcrypto3",
        libraryName = "libcrypto3",
        visibility = Visibility.public
    ) {
//        headers { it.name.value.startsWith("openssl/") }
//        typedefs {
//            when (it.name.value) {
//                "OSSL_PARAM", "EVP_MAC" -> true
//                else                    -> false
//            }
//        }
        includeFunctions(recursive = true) {
            when (it.name.value) {
//                "ERR_error_string", "ERR_get_error" -> true
                "EVP_DigestSignInit_ex" -> true
                else                    -> false
            }
        }
    }

    generator.generateCommon().forEach {
        println("Path: '${it.path}'")
        println("Content:")
        println(it.content)
        println("--------------------------------------")
    }
}

private fun test(index: CxIndex) {
    index.headers.forEach { header ->
        when (header.name.value) {
            "openssl/types.h",
            "openssl/core.h" -> {
                return@forEach
                header.typedefs.forEach { typedef ->
                    when (typedef.name.value) {
                        "OSSL_PARAM", "EVP_MAC" -> {
                            println(typedef.toKotlinDeclaration(index, Visibility.public))
                            println()
                        }
                    }
                }

                header.structs.forEach { struct ->
                    when (struct.name.value) {
                        "evp_mac_st", "ossl_param_st" -> {
                            println(struct.toKotlinDeclaration(index))

                            println()
                            println("-------------------------------")
                            println()
                        }
                    }
                }
            }
            "openssl/err.h"  -> {
//                header.typedefs.forEach { typedef ->
//                    when (typedef.name.value) {
//                        "lh_ERR_STRING_DATA_compfunc" -> println(typedef.toKotlinDeclaration(index))
//                    }
//                }
                header.functions.forEach { function ->
                    when (function.name.value) {
                        "ERR_error_string", "ERR_get_error" -> {
                            println(function.toKotlinExpectDeclaration(index, Visibility.public))
                            println()
                            println(function.toKotlinJniDeclaration(index, "libCrypto3", actual = true, Visibility.public))

//                            println(
//                                function.toCJniDeclaration(
//                                    "dev.whyoleg.ffi.libcrypto",
//                                    "err"
//                                )
//                            )
//                            println()
//                            println(
//                                function.toCEmscriptenDeclaration()
//                            )
//                            println()
//                            println(
//                                function.toCNativeBitcodeDeclaration()
//                            )
                            println()
                            println("-------------------------------")
                            println()
                        }
                    }
                }
            }
        }
    }
}
