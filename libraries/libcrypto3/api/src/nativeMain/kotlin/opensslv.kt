@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual const val OPENSSL_VERSION_STRING: Int = dev.whyoleg.ffi.libcrypto3.cinterop.OPENSSL_VERSION_STRING

actual fun OpenSSL_version(type: Int): CString? = dev.whyoleg.ffi.libcrypto3.cinterop.OpenSSL_version(type)

actual fun OPENSSL_version_major(): UInt = dev.whyoleg.ffi.libcrypto3.cinterop.OPENSSL_version_major()
