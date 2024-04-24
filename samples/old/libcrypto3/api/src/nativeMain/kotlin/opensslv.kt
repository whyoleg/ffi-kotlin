@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import kotlinx.cinterop.internal.*

actual const val OPENSSL_VERSION_STRING: Int = 6 //magic :)

actual fun OpenSSL_version(type: Int): CString? = libCrypto3ImplicitScope.unsafe {
    CPointer(CType.Byte, ffi_OpenSSL_version(type))
}

actual fun OPENSSL_version_major(): UInt = libCrypto3ImplicitScope.unsafe {
    ffi_OPENSSL_version_major().toUInt()
}


@CCall("ffi_OpenSSL_version")
private external fun ffi_OpenSSL_version(type: Int): Long

@CCall("ffi_OPENSSL_version_major")
private external fun ffi_OPENSSL_version_major(): Int

