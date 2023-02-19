package dev.whyoleg.ffi.libcrypto3.test

import dev.whyoleg.ffi.*
import dev.whyoleg.ffi.libcrypto3.*


internal fun checkError(result: Int): Int {
    if (result > 0) return result
    fail(result)
}

private fun fail(result: Int): Nothing {
    val code = ERR_get_error()
    println("[result: $result, code: $code]")
    val message = ERR_error_string(code, null)?.toKString()
    error("OPENSSL failure [result: $result, code: $code]: $message")
}
