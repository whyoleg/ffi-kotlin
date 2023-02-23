#include <jni.h>
#include <openssl/opensslv.h>
#include <openssl/crypto.h>
#include <openssl/evp.h>

JNIEXPORT jlong JNICALL Java_dev_whyoleg_ffi_libcrypto3_opensslv_OpenSSL_1version (JNIEnv* env, jclass jclss,
  jint p_type
) {
    return (jlong)OpenSSL_version(p_type);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_opensslv_OPENSSL_1version_1major (JNIEnv* env, jclass jclss) {
    return (jint)OPENSSL_version_major();
}

//evpmd start

JNIEXPORT jlong JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmd_EVP_1MD_1fetch (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_algorithm,
  jlong p_properties
) {
    return (jlong)EVP_MD_fetch((OSSL_LIB_CTX*)p_ctx, (char*)p_algorithm, (char*)p_properties);
}

JNIEXPORT void JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmd_EVP_1MD_1free (JNIEnv* env, jclass jclss,
  jlong p_ctx
) {
    EVP_MD_free((EVP_MD*)p_ctx);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmd_EVP_1MD_1get_1size (JNIEnv* env, jclass jclss,
  jlong p_ctx
) {
    return (jint)EVP_MD_get_size((EVP_MD*)p_ctx);
}

JNIEXPORT jlong JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmd_EVP_1MD_1CTX_1new (JNIEnv* env, jclass jclss) {
    return (jlong)EVP_MD_CTX_new();
}

JNIEXPORT void JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmd_EVP_1MD_1CTX_1free (JNIEnv* env, jclass jclss,
  jlong p_ctx
) {
    EVP_MD_CTX_free((EVP_MD_CTX*)p_ctx);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmd_EVP_1DigestInit (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_type
) {
    return (jint)EVP_DigestInit((EVP_MD_CTX*)p_ctx, (EVP_MD*)p_type);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmd_EVP_1DigestUpdate (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_d,
  jlong p_cnt
) {
    return (jint)EVP_DigestUpdate((EVP_MD_CTX*)p_ctx, (void*)p_d, p_cnt);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmd_EVP_1DigestFinal (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_md,
  jlong p_cnt
) {
    return (jint)EVP_DigestFinal((EVP_MD_CTX*)p_ctx, (unsigned char*)p_md, (unsigned int*)p_cnt);
}

//evpmd end
