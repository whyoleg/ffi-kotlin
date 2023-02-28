package dev.whyoleg.ffi.libcrypto3.test

import dev.whyoleg.ffi.*
import dev.whyoleg.ffi.libcrypto3.*


internal fun checkError(result: Int): Int {
    if (result > 0) return result
    fail(result)
}

private fun fail(result: Int): Nothing {
    val code = ERR_get_error()
    val message = ERR_error_string(code, null)?.toKString()
    throw OpensslException("OPENSSL failure [result: $result, code: $code]: $message")
}

class OpensslException(message: String) : Throwable(message)
