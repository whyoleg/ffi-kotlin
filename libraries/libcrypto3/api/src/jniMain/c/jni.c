#include <jni.h>
#include <openssl/opensslv.h>
#include <openssl/crypto.h>
#include <openssl/evp.h>
#include <openssl/err.h>

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

//osslparam start

JNIEXPORT void JNICALL Java_dev_whyoleg_ffi_libcrypto3_osslparam_OSSL_1PARAM_1construct_1utf8_1string (JNIEnv* env, jclass jclss,
  jlong p_key,
  jlong p_buf,
  jlong p_bsize,
  jlong p_returnPointer
) {
    *(OSSL_PARAM*)p_returnPointer = OSSL_PARAM_construct_utf8_string((char*)p_key, (char*)p_buf, p_bsize);
}

JNIEXPORT void JNICALL Java_dev_whyoleg_ffi_libcrypto3_osslparam_OSSL_1PARAM_1construct_1end (JNIEnv* env, jclass jclss,
  jlong p_returnPointer
) {
    *(OSSL_PARAM*)p_returnPointer = OSSL_PARAM_construct_end();
}

//osslparam end

//evpmac start


JNIEXPORT jlong JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmac_EVP_1MAC_1fetch (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_algorithm,
  jlong p_properties
) {
    return (jlong)EVP_MAC_fetch((OSSL_LIB_CTX*)p_ctx, (char*)p_algorithm, (char*)p_properties);
}

JNIEXPORT jlong JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmac_EVP_1MAC_1CTX_1new (JNIEnv* env, jclass jclss,
  jlong p_ctx
) {
    return (jlong)EVP_MAC_CTX_new((EVP_MAC*)p_ctx);
}

JNIEXPORT jlong JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmac_EVP_1MAC_1CTX_1get_1mac_1size (JNIEnv* env, jclass jclss,
  jlong p_ctx
) {
    return (jlong)EVP_MAC_CTX_get_mac_size((EVP_MAC_CTX*)p_ctx);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmac_EVP_1MAC_1init (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_key,
  jlong p_keylen,
  jlong p_params
) {
    return (jint)EVP_MAC_init((EVP_MAC_CTX*)p_ctx, (unsigned char*)p_key, p_keylen, (OSSL_PARAM*)p_params);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmac_EVP_1MAC_1update (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_data,
  jlong p_datalen
) {
    return (jint)EVP_MAC_update((EVP_MAC_CTX*)p_ctx, (unsigned char*)p_data, p_datalen);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmac_EVP_1MAC_1final (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_out,
  jlong p_outl,
  jlong p_outsize
) {
    return (jint)EVP_MAC_final((EVP_MAC_CTX*)p_ctx, (unsigned char*)p_out, (unsigned long*)p_outl, p_outsize);
}

JNIEXPORT void JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmac_EVP_1MAC_1CTX_1free (JNIEnv* env, jclass jclss,
  jlong p_ctx
) {
    EVP_MAC_CTX_free((EVP_MAC_CTX*)p_ctx);
}

JNIEXPORT void JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpmac_EVP_1MAC_1free (JNIEnv* env, jclass jclss,
  jlong p_ctx
) {
    EVP_MAC_free((EVP_MAC*)p_ctx);
}

//evpmac end

//err start


JNIEXPORT jlong JNICALL Java_dev_whyoleg_ffi_libcrypto3_err_ERR_1get_1error (JNIEnv* env, jclass jclss) {
    return (jlong)ERR_get_error();
}

JNIEXPORT jlong JNICALL Java_dev_whyoleg_ffi_libcrypto3_err_ERR_1error_1string (JNIEnv* env, jclass jclss,
  jlong p_e,
  jlong p_buf
) {
    return (jlong)ERR_error_string(p_e, (char*)p_buf);
}

//err end

//evppkey start

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evppkey_EVP_1PKEY_1keygen_1init (JNIEnv* env, jclass jclss,
  jlong p_ctx
) {
    return (jint)EVP_PKEY_keygen_init((EVP_PKEY_CTX*)p_ctx);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evppkey_EVP_1PKEY_1generate (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_ppkey
) {
    return (jint)EVP_PKEY_generate((EVP_PKEY_CTX*)p_ctx, (EVP_PKEY**)p_ppkey);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evppkey_EVP_1PKEY_1up_1ref (JNIEnv* env, jclass jclss,
  jlong p_pkey
) {
    return (jint)EVP_PKEY_up_ref((EVP_PKEY*)p_pkey);
}

JNIEXPORT void JNICALL Java_dev_whyoleg_ffi_libcrypto3_evppkey_EVP_1PKEY_1free (JNIEnv* env, jclass jclss,
  jlong p_pkey
) {
    EVP_PKEY_free((EVP_PKEY*)p_pkey);
}

//evppkey end
//evppkeyctx start

JNIEXPORT jlong JNICALL Java_dev_whyoleg_ffi_libcrypto3_evppkeyctx_EVP_1PKEY_1CTX_1new_1from_1name (JNIEnv* env, jclass jclss,
  jlong p_libctx,
  jlong p_name,
  jlong p_propquery
) {
    return (jlong)EVP_PKEY_CTX_new_from_name((OSSL_LIB_CTX*)p_libctx, (char*)p_name, (char*)p_propquery);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evppkeyctx_EVP_1PKEY_1CTX_1set_1params (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_params
) {
    return (jint)EVP_PKEY_CTX_set_params((EVP_PKEY_CTX*)p_ctx, (OSSL_PARAM*)p_params);
}

JNIEXPORT void JNICALL Java_dev_whyoleg_ffi_libcrypto3_evppkeyctx_EVP_1PKEY_1CTX_1free (JNIEnv* env, jclass jclss,
  jlong p_ctx
) {
    EVP_PKEY_CTX_free((EVP_PKEY_CTX*)p_ctx);
}

//evppkeyctx end
//evpdigest start

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpdigest_EVP_1DigestSignInit_1ex (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_pctx,
  jlong p_mdname,
  jlong p_libctx,
  jlong p_props,
  jlong p_pkey,
  jlong p_params
) {
    return (jint)EVP_DigestSignInit_ex(
        (EVP_MD_CTX*)p_ctx,
        (EVP_PKEY_CTX**)p_pctx,
        (char*)p_mdname,
        (OSSL_LIB_CTX*)p_libctx,
        (char*)p_props,
        (EVP_PKEY*)p_pkey,
        (OSSL_PARAM*)p_params
    );
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpdigest_EVP_1DigestSignUpdate (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_data,
  jlong p_dsize
) {
    return (jint)EVP_DigestSignUpdate((EVP_MD_CTX*)p_ctx, (void*)p_data, p_dsize);
}
JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpdigest_EVP_1DigestSignFinal (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_sigret,
  jlong p_siglen
) {
    return (jint)EVP_DigestSignFinal((EVP_MD_CTX*)p_ctx, (unsigned char*)p_sigret, (unsigned long*)p_siglen);
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpdigest_EVP_1DigestVerifyInit_1ex (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_pctx,
  jlong p_mdname,
  jlong p_libctx,
  jlong p_props,
  jlong p_pkey,
  jlong p_params
) {
    return (jint)EVP_DigestVerifyInit_ex(
        (EVP_MD_CTX*)p_ctx,
        (EVP_PKEY_CTX**)p_pctx,
        (char*)p_mdname,
        (OSSL_LIB_CTX*)p_libctx,
        (char*)p_props,
        (EVP_PKEY*)p_pkey,
        (OSSL_PARAM*)p_params
    );
}

JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpdigest_EVP_1DigestVerifyUpdate (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_data,
  jlong p_dsize
) {
    return (jint)EVP_DigestVerifyUpdate((EVP_MD_CTX*)p_ctx, (void*)p_data, p_dsize);
}
JNIEXPORT jint JNICALL Java_dev_whyoleg_ffi_libcrypto3_evpdigest_EVP_1DigestVerifyFinal (JNIEnv* env, jclass jclss,
  jlong p_ctx,
  jlong p_sig,
  jlong p_siglen
) {
    return (jint)EVP_DigestVerifyFinal((EVP_MD_CTX*)p_ctx, (unsigned char*)p_sig, p_siglen);
}


//evpdigest end
