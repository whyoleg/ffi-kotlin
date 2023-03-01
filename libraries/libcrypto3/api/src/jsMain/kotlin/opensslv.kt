@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual const val OPENSSL_VERSION_STRING: Int = 6 //magic :)

actual fun OpenSSL_version(type: Int): CString? = CString(NativePointer(opensslv.OpenSSL_version(type)))

actual fun OPENSSL_version_major(): UInt = opensslv.OPENSSL_version_major().toUInt()

@JsModule("ffi-libcrypto")
@JsNonModule
@JsName("Module")
private external object opensslv {
    @JsName("_ffi_OpenSSL_version")
    fun OpenSSL_version(type: Int): Int

    @JsName("_ffi_OPENSSL_version_major")
    fun OPENSSL_version_major(): Int
}
