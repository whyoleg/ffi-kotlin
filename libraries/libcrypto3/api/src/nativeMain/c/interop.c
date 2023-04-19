#include <openssl/opensslv.h>
#include <openssl/crypto.h>
#include <openssl/err.h>
#include <openssl/evp.h>

const void* ffi_OpenSSL_version_call __asm("ffi_OpenSSL_version");
const void* ffi_OpenSSL_version_call = (const void*)&OpenSSL_version;

const void* ffi_OPENSSL_version_major_call __asm("ffi_OPENSSL_version_major");
const void* ffi_OPENSSL_version_major_call = (const void*)&OPENSSL_version_major;

const void* ffi_ERR_get_error_call __asm("ffi_ERR_get_error");
const void* ffi_ERR_get_error_call = (const void*)&ERR_get_error;

const void* ffi_ERR_error_string_call __asm("ffi_ERR_error_string");
const void* ffi_ERR_error_string_call = (const void*)&ERR_error_string;

//evpmd start

const void* ffi_EVP_MD_get_size_call __asm("ffi_EVP_MD_get_size");
const void* ffi_EVP_MD_get_size_call = (const void*)&EVP_MD_get_size;

const void* ffi_EVP_MD_CTX_new_call __asm("ffi_EVP_MD_CTX_new");
const void* ffi_EVP_MD_CTX_new_call = (const void*)&EVP_MD_CTX_new;

const void* ffi_EVP_MD_fetch_call __asm("ffi_EVP_MD_fetch");
const void* ffi_EVP_MD_fetch_call = (const void*)&EVP_MD_fetch;

const void* ffi_EVP_MD_CTX_free_call __asm("ffi_EVP_MD_CTX_free");
const void* ffi_EVP_MD_CTX_free_call = (const void*)&EVP_MD_CTX_free;

const void* ffi_EVP_MD_free_call __asm("ffi_EVP_MD_free");
const void* ffi_EVP_MD_free_call = (const void*)&EVP_MD_free;

const void* ffi_EVP_DigestFinal_call __asm("ffi_EVP_DigestFinal");
const void* ffi_EVP_DigestFinal_call = (const void*)&EVP_DigestFinal;

const void* ffi_EVP_DigestUpdate_call __asm("ffi_EVP_DigestUpdate");
const void* ffi_EVP_DigestUpdate_call = (const void*)&EVP_DigestUpdate;

const void* ffi_EVP_DigestInit_call __asm("ffi_EVP_DigestInit");
const void* ffi_EVP_DigestInit_call = (const void*)&EVP_DigestInit;

//evpmd end

//osslparam start

__attribute__((always_inline))
void interop_OSSL_PARAM_construct_utf8_string (
  char* p_key,
  char* p_buf,
  unsigned int p_bsize,
  OSSL_PARAM* p_returnPointer
) {
    *p_returnPointer = OSSL_PARAM_construct_utf8_string(p_key, p_buf, p_bsize);
}

const void* ffi_OSSL_PARAM_construct_utf8_string_call __asm("ffi_OSSL_PARAM_construct_utf8_string");
const void* ffi_OSSL_PARAM_construct_utf8_string_call = (const void*)&interop_OSSL_PARAM_construct_utf8_string;

__attribute__((always_inline))
void interop_OSSL_PARAM_construct_end (
  OSSL_PARAM* p_returnPointer
) {
    *p_returnPointer = OSSL_PARAM_construct_end();
}

const void* ffi_OSSL_PARAM_construct_end_call __asm("ffi_OSSL_PARAM_construct_end");
const void* ffi_OSSL_PARAM_construct_end_call = (const void*)&interop_OSSL_PARAM_construct_end;

//osslparam end

//evpmac start

const void* ffi_EVP_MAC_fetch_call __asm("ffi_EVP_MAC_fetch");
const void* ffi_EVP_MAC_fetch_call = (const void*)&EVP_MAC_fetch;

const void* ffi_EVP_MAC_free_call __asm("ffi_EVP_MAC_free");
const void* ffi_EVP_MAC_free_call = (const void*)&EVP_MAC_free;

const void* ffi_EVP_MAC_CTX_free_call __asm("ffi_EVP_MAC_CTX_free");
const void* ffi_EVP_MAC_CTX_free_call = (const void*)&EVP_MAC_CTX_free;

const void* ffi_EVP_MAC_final_call __asm("ffi_EVP_MAC_final");
const void* ffi_EVP_MAC_final_call = (const void*)&EVP_MAC_final;

const void* ffi_EVP_MAC_update_call __asm("ffi_EVP_MAC_update");
const void* ffi_EVP_MAC_update_call = (const void*)&EVP_MAC_update;

const void* ffi_EVP_MAC_init_call __asm("ffi_EVP_MAC_init");
const void* ffi_EVP_MAC_init_call = (const void*)&EVP_MAC_init;

const void* ffi_EVP_MAC_CTX_get_mac_size_call __asm("ffi_EVP_MAC_CTX_get_mac_size");
const void* ffi_EVP_MAC_CTX_get_mac_size_call = (const void*)&EVP_MAC_CTX_get_mac_size;

const void* ffi_EVP_MAC_CTX_new_call __asm("ffi_EVP_MAC_CTX_new");
const void* ffi_EVP_MAC_CTX_new_call = (const void*)&EVP_MAC_CTX_new;

//evpmac end

//evppkey start

const void* ffi_EVP_PKEY_keygen_init_call __asm("ffi_EVP_PKEY_keygen_init");
const void* ffi_EVP_PKEY_keygen_init_call = (const void*)&EVP_PKEY_keygen_init;

const void* ffi_EVP_PKEY_free_call __asm("ffi_EVP_PKEY_free");
const void* ffi_EVP_PKEY_free_call = (const void*)&EVP_PKEY_free;

const void* ffi_EVP_PKEY_up_ref_call __asm("ffi_EVP_PKEY_up_ref");
const void* ffi_EVP_PKEY_up_ref_call = (const void*)&EVP_PKEY_up_ref;

const void* ffi_EVP_PKEY_generate_call __asm("ffi_EVP_PKEY_generate");
const void* ffi_EVP_PKEY_generate_call = (const void*)&EVP_PKEY_generate;

//evppkey end
//evppkeyctx start

const void* ffi_EVP_PKEY_CTX_new_from_name_call __asm("ffi_EVP_PKEY_CTX_new_from_name");
const void* ffi_EVP_PKEY_CTX_new_from_name_call = (const void*)&EVP_PKEY_CTX_new_from_name;

const void* ffi_EVP_PKEY_CTX_free_call __asm("ffi_EVP_PKEY_CTX_free");
const void* ffi_EVP_PKEY_CTX_free_call = (const void*)&EVP_PKEY_CTX_free;

const void* ffi_EVP_PKEY_CTX_set_params_call __asm("ffi_EVP_PKEY_CTX_set_params");
const void* ffi_EVP_PKEY_CTX_set_params_call = (const void*)&EVP_PKEY_CTX_set_params;

//evppkeyctx end
//evpdigest start

const void* ffi_EVP_DigestSignInit_ex_call __asm("ffi_EVP_DigestSignInit_ex");
const void* ffi_EVP_DigestSignInit_ex_call = (const void*)&EVP_DigestSignInit_ex;

const void* ffi_EVP_DigestVerifyFinal_call __asm("ffi_EVP_DigestVerifyFinal");
const void* ffi_EVP_DigestVerifyFinal_call = (const void*)&EVP_DigestVerifyFinal;

const void* ffi_EVP_DigestVerifyUpdate_call __asm("ffi_EVP_DigestVerifyUpdate");
const void* ffi_EVP_DigestVerifyUpdate_call = (const void*)&EVP_DigestVerifyUpdate;

const void* ffi_EVP_DigestVerifyInit_ex_call __asm("ffi_EVP_DigestVerifyInit_ex");
const void* ffi_EVP_DigestVerifyInit_ex_call = (const void*)&EVP_DigestVerifyInit_ex;

const void* ffi_EVP_DigestSignFinal_call __asm("ffi_EVP_DigestSignFinal");
const void* ffi_EVP_DigestSignFinal_call = (const void*)&EVP_DigestSignFinal;

const void* ffi_EVP_DigestSignUpdate_call __asm("ffi_EVP_DigestSignUpdate");
const void* ffi_EVP_DigestSignUpdate_call = (const void*)&EVP_DigestSignUpdate;

//evpdigest end
