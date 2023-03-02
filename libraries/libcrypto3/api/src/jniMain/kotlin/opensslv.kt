@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual const val OPENSSL_VERSION_STRING: Int = 6 //magic :)

actual fun OpenSSL_version(type: Int): CString? = nativeCString(opensslv.OpenSSL_version(type))

actual fun OPENSSL_version_major(): UInt = opensslv.OPENSSL_version_major().toUInt()

private object opensslv {
    init {
        JNI
    }

    @JvmStatic
    external fun OpenSSL_version(type: Int): Long

    @JvmStatic
    external fun OPENSSL_version_major(): Int
}
