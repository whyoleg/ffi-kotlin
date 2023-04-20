package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*
import okio.*
import okio.Path.Companion.toPath

public fun main() {
    val index = FileSystem.SYSTEM.readCxIndex(
        "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-cx-index/build/index.json".toPath()
    )

    index.filter {
        typedefs {
            when (it.name.value) {
                "OSSL_PARAM", "EVP_MAC" -> true
                else                    -> false
            }
        }
        structs { false }
        enums { false }
        functions { false }
    }.toPrettyString().also(::println)

    return

    ForeignCGenerator(index, "dev.whyoleg.ffi.libcrypto3", "libcrypto3") {
        headers { it.name.value.startsWith("openssl/") }
        typedefs {
            when (it.name.value) {
                "OSSL_PARAM", "EVP_MAC" -> true
                else                    -> false
            }
        }
        functions {
            when (it.name.value) {
                "ERR_error_string", "ERR_get_error" -> true
                else                                -> false
            }
        }
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
                            println(typedef.toKotlinDeclaration(index))
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
                            println(function.toKotlinCommonDeclaration(index, expect = true, Visibility.public))
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
