@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*

actual const val OPENSSL_VERSION_STRING: Int = 6 //magic :)

actual fun OpenSSL_version(type: Int): CString? = libCrypto3ImplicitScope.unsafe {
    CPointer(CType.Byte, opensslv.OpenSSL_version(type))
}

actual fun OPENSSL_version_major(): UInt = libCrypto3ImplicitScope.unsafe {
    opensslv.OPENSSL_version_major().toUInt()
}

private object opensslv {
    @JvmStatic
    external fun OpenSSL_version(type: Int): Long

    @JvmStatic
    external fun OPENSSL_version_major(): Int
}