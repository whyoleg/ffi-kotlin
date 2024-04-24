@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*

expect val OPENSSL_VERSION_STRING: Int

expect fun OpenSSL_version(type: Int): CString?

expect fun OPENSSL_version_major(): UInt
