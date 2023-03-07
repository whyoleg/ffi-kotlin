package dev.whyoleg.ffi.c.generator

import dev.whyoleg.ffi.c.index.*

fun main() {
//    val index = buildCIndex(listOf("clang-c/Index.h"), listOf("/opt/homebrew/opt/llvm/include"))


    val index = buildCIndex(
        headers = listOf(
            "openssl/err.h",
            "openssl/evp.h",
//            "openssl/ssl.h",
        ),
        includeDirectories = listOf(
            "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/openssl/prebuilt/macos-arm64/include"
        )
    )
//    return
//    index.functions.forEach { (usr, info) ->
//        if (info.include == "openssl/err.h") println("$usr -> $info")
//    }
//    println()
//    index.structs.forEach { (usr, info) ->
//        println("$usr -> $info")
//    }
//    println()
//    index.typedefs.forEach { (usr, info) ->
//        println("$usr -> $info")
//    }
//    println()
//    index.enums.forEach { (usr, info) ->
//        println("$usr -> $info")
//    }
}
