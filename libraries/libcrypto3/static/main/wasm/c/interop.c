#include <emscripten.h>
#include <openssl/opensslv.h>
#include <openssl/crypto.h>
#include <openssl/err.h>
#include <openssl/evp.h>
#include <openssl/sha.h>

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
