package dev.whyoleg.foreign.gradle.dsl.c

import org.gradle.api.provider.*

// these are 'soft' filters:
// it means that referenced declarations will be still included
// f.e if we only include `openssl/*` headers
// and in some declaration (X), there will be reference to some declaration (Y) from `stdio.h` header
// this declaration (Y) will be also included (not referenced declarations will not be included)
// otherwise there will be code, which could not be compiled
// String is a wildcard pattern
public interface ForeignCBindings {
    public fun packageName(block: (header: String) -> String)

    public val initialHeaders: ListProperty<String>
    public fun initialHeaders(vararg headers: String)

    // filters

    public val includedHeaderPatterns: ListProperty<String>
    public val excludedHeaderPatterns: ListProperty<String>

    public val includedVariablePatterns: ListProperty<String>
    public val excludedVariablePatterns: ListProperty<String>

    public val includedEnumPatterns: ListProperty<String>
    public val excludedEnumPatterns: ListProperty<String>

    public val includedTypedefPatterns: ListProperty<String>
    public val excludedTypedefPatterns: ListProperty<String>

    public val includedRecordPatterns: ListProperty<String>
    public val excludedRecordPatterns: ListProperty<String>

    public val includedFunctionPatterns: ListProperty<String>
    public val excludedFunctionPatterns: ListProperty<String>

    public fun includeHeaders(vararg patterns: String)
    public fun excludeHeaders(vararg patterns: String)

    public fun includeVariables(vararg patterns: String)
    public fun excludeVariables(vararg patterns: String)

    public fun includeEnums(vararg patterns: String)
    public fun excludeEnums(vararg patterns: String)

    public fun includeTypedefs(vararg patterns: String)
    public fun excludeTypedefs(vararg patterns: String)

    public fun includeRecords(vararg patterns: String)
    public fun excludeRecords(vararg patterns: String)

    public fun includeFunctions(vararg patterns: String)
    public fun excludeFunctions(vararg patterns: String)
}
