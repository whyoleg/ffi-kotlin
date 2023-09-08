package dev.whyoleg.foreign.cx.index

import dev.whyoleg.foreign.cx.index.*
import kotlinx.serialization.*
import kotlin.jvm.*

@Serializable
@JvmInline
public value class CxBindingPackageName(public val value: String)

@Serializable
public data class CxBindingPackageInfo(
    val typedefs: Map<CxDeclarationName, CxDeclarationId>,
    val records: Map<CxDeclarationName, CxDeclarationId>,
    val enums: Map<CxDeclarationName, CxDeclarationId>,
    val functions: Map<CxDeclarationName, CxDeclarationId>,
)

//int BN_abs_is_word(const BIGNUM *a, const BN_ULONG w);
//int BN_is_zero(const BIGNUM *a);
//int BN_is_one(const BIGNUM *a);
//int BN_is_word(const BIGNUM *a, const BN_ULONG w);
//int BN_is_odd(const BIGNUM *a);

//int OPENSSL_buf2hexstr_ex(char *str, size_t str_n, size_t *strlength,
//                          const unsigned char *buf, size_t buflen,
//                          const char sep);
//char *OPENSSL_buf2hexstr(const unsigned char *buf, long buflen);

@Serializable
public data class CxBPackage(
    val name: CxBindingPackageName,
    val typedefs: Map<CxDeclarationName, Map<String, CxTypedefInfo>>,
    val records: Map<CxDeclarationName, Map<String, CxRecordInfo>>,
    val enums: Map<CxDeclarationName, Map<String, CxEnumInfo>>,
    val functions: Map<CxDeclarationName, Map<String, CxFunctionInfo>>,
)
