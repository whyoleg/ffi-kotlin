#include <emscripten.h>
#include <openssl/opensslv.h>
#include <openssl/crypto.h>
#include <openssl/err.h>
#include <openssl/evp.h>

EMSCRIPTEN_KEEPALIVE const char* ffi_OpenSSL_version(int p_type) {
    return OpenSSL_version(p_type);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_OPENSSL_version_major() {
    return OPENSSL_version_major();
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_ERR_get_error () {
    return ERR_get_error();
}

EMSCRIPTEN_KEEPALIVE char* ffi_ERR_error_string (
  unsigned int p_e,
  char* p_buf
) {
    return ERR_error_string(p_e, p_buf);
}


//evpmd start

EMSCRIPTEN_KEEPALIVE EVP_MD* ffi_EVP_MD_fetch (
  OSSL_LIB_CTX* p_ctx,
  char* p_algorithm,
  char* p_properties
) {
    return EVP_MD_fetch(p_ctx, p_algorithm, p_properties);
}

EMSCRIPTEN_KEEPALIVE void ffi_EVP_MD_free (
  EVP_MD* p_ctx
) {
    EVP_MD_free(p_ctx);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_MD_get_size (
  EVP_MD* p_ctx
) {
    return EVP_MD_get_size(p_ctx);
}

EMSCRIPTEN_KEEPALIVE EVP_MD_CTX* ffi_EVP_MD_CTX_new () {
    return EVP_MD_CTX_new();
}

EMSCRIPTEN_KEEPALIVE void ffi_EVP_MD_CTX_free (
  EVP_MD_CTX* p_ctx
) {
    EVP_MD_CTX_free(p_ctx);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_DigestInit (
  EVP_MD_CTX* p_ctx,
  EVP_MD* p_type
) {
    return EVP_DigestInit(p_ctx, p_type);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_DigestUpdate (
  EVP_MD_CTX* p_ctx,
  void* p_d,
  unsigned int p_cnt
) {
    return EVP_DigestUpdate(p_ctx, p_d, p_cnt);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_DigestFinal (
  EVP_MD_CTX* p_ctx,
  unsigned char* p_md,
  unsigned int* p_cnt
) {
    return EVP_DigestFinal(p_ctx, p_md, p_cnt);
}

//evpmd end

//osslparam start

EMSCRIPTEN_KEEPALIVE void ffi_OSSL_PARAM_construct_utf8_string (
  char* p_key,
  char* p_buf,
  unsigned int p_bsize,
  OSSL_PARAM* p_returnPointer
) {
    *p_returnPointer = OSSL_PARAM_construct_utf8_string(p_key, p_buf, p_bsize);
}

EMSCRIPTEN_KEEPALIVE void ffi_OSSL_PARAM_construct_end (
  OSSL_PARAM* p_returnPointer
) {
    *p_returnPointer = OSSL_PARAM_construct_end();
}

//osslparam end

//evpmac start

EMSCRIPTEN_KEEPALIVE EVP_MAC* ffi_EVP_MAC_fetch (
  OSSL_LIB_CTX* p_ctx,
  char* p_algorithm,
  char* p_properties
) {
    return EVP_MAC_fetch(p_ctx, p_algorithm, p_properties);
}

EMSCRIPTEN_KEEPALIVE EVP_MAC_CTX* ffi_EVP_MAC_CTX_new (
  EVP_MAC* p_ctx
) {
    return EVP_MAC_CTX_new(p_ctx);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_MAC_CTX_get_mac_size (
  EVP_MAC_CTX* p_ctx
) {
    return EVP_MAC_CTX_get_mac_size(p_ctx);
}

EMSCRIPTEN_KEEPALIVE int ffi_EVP_MAC_init (
  EVP_MAC_CTX* p_ctx,
  unsigned char* p_key,
  unsigned int p_keylen,
  OSSL_PARAM* p_params
) {
    return EVP_MAC_init(p_ctx, p_key, p_keylen, p_params);
}

EMSCRIPTEN_KEEPALIVE int ffi_EVP_MAC_update (
  EVP_MAC_CTX* p_ctx,
  unsigned char* p_data,
  unsigned int p_datalen
) {
    return EVP_MAC_update(p_ctx, p_data, p_datalen);
}

EMSCRIPTEN_KEEPALIVE int ffi_EVP_MAC_final (
  EVP_MAC_CTX* p_ctx,
  unsigned char* p_out,
  unsigned long* p_outl,
  unsigned int p_outsize
) {
    return EVP_MAC_final(p_ctx, p_out, p_outl, p_outsize);
}

EMSCRIPTEN_KEEPALIVE void ffi_EVP_MAC_CTX_free (
  EVP_MAC_CTX* p_ctx
) {
    EVP_MAC_CTX_free(p_ctx);
}

EMSCRIPTEN_KEEPALIVE void ffi_EVP_MAC_free (
  EVP_MAC* p_ctx
) {
    EVP_MAC_free(p_ctx);
}

//evpmac end

//evppkey start

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_PKEY_keygen_init (
  EVP_PKEY_CTX* p_ctx
) {
    return EVP_PKEY_keygen_init(p_ctx);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_PKEY_generate (
  EVP_PKEY_CTX* p_ctx,
  EVP_PKEY** p_ppkey
) {
    return EVP_PKEY_generate(p_ctx, p_ppkey);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_PKEY_up_ref (
  EVP_PKEY* p_pkey
) {
    return EVP_PKEY_up_ref(p_pkey);
}

EMSCRIPTEN_KEEPALIVE void ffi_EVP_PKEY_free (
  EVP_PKEY* p_pkey
) {
    EVP_PKEY_free(p_pkey);
}

//evppkey end
//evppkeyctx start

EMSCRIPTEN_KEEPALIVE EVP_PKEY_CTX* ffi_EVP_PKEY_CTX_new_from_name (
  OSSL_LIB_CTX* p_libctx,
  char* p_name,
  char* p_propquery
) {
    return EVP_PKEY_CTX_new_from_name(p_libctx, p_name, p_propquery);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_PKEY_CTX_set_params (
  EVP_PKEY_CTX* p_ctx,
  OSSL_PARAM* p_params
) {
    return EVP_PKEY_CTX_set_params(p_ctx, p_params);
}

EMSCRIPTEN_KEEPALIVE void ffi_EVP_PKEY_CTX_free (
  EVP_PKEY_CTX* p_ctx
) {
    EVP_PKEY_CTX_free(p_ctx);
}

//evppkeyctx end
//evpdigest start

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_DigestSignInit_ex (
  EVP_MD_CTX* p_ctx,
  EVP_PKEY_CTX** p_pctx,
  char* p_mdname,
  OSSL_LIB_CTX* p_libctx,
  char* p_props,
  EVP_PKEY* p_pkey,
  OSSL_PARAM* p_params
) {
    return EVP_DigestSignInit_ex(
        p_ctx,
        p_pctx,
        p_mdname,
        p_libctx,
        p_props,
        p_pkey,
        p_params
    );
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_DigestSignUpdate (
  EVP_MD_CTX* p_ctx,
  void* p_data,
  unsigned int p_dsize
) {
    return EVP_DigestSignUpdate(p_ctx, p_data, p_dsize);
}
EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_DigestSignFinal (
  EVP_MD_CTX* p_ctx,
  unsigned char* p_sigret,
  unsigned long* p_siglen
) {
    return EVP_DigestSignFinal(p_ctx, p_sigret, p_siglen);
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_DigestVerifyInit_ex (
  EVP_MD_CTX* p_ctx,
  EVP_PKEY_CTX** p_pctx,
  char* p_mdname,
  OSSL_LIB_CTX* p_libctx,
  char* p_props,
  EVP_PKEY* p_pkey,
  OSSL_PARAM* p_params
) {
    return EVP_DigestVerifyInit_ex(
        p_ctx,
        p_pctx,
        p_mdname,
        p_libctx,
        p_props,
        p_pkey,
        p_params
    );
}

EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_DigestVerifyUpdate (
  EVP_MD_CTX* p_ctx,
  void* p_data,
  unsigned int p_dsize
) {
    return EVP_DigestVerifyUpdate(p_ctx, p_data, p_dsize);
}
EMSCRIPTEN_KEEPALIVE unsigned int ffi_EVP_DigestVerifyFinal (
  EVP_MD_CTX* p_ctx,
  unsigned char* p_sig,
  unsigned int p_siglen
) {
    return EVP_DigestVerifyFinal(p_ctx, p_sig, p_siglen);
}

//evpdigest end
