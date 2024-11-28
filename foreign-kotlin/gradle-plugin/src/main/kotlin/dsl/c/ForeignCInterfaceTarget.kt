package dev.whyoleg.foreign.gradle.dsl.c

import dev.whyoleg.foreign.gradle.dsl.*
import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.jetbrains.kotlin.gradle.plugin.*

public interface ForeignCInterfaceTarget : Named {
    public val kotlinPlatformType: KotlinPlatformType
    public val foreignTarget: ForeignTarget

    // configuration

    public val headerSearchDirectories: ListProperty<Directory>
    public val librarySearchDirectories: ListProperty<Directory>
    public val libraryNames: ListProperty<String>

    //public val linkerArgs: ListProperty<String>

    public val initialHeaders: ListProperty<String>
    public fun initialHeaders(vararg headers: String)

    // these are 'soft' filters:
    // it means that referenced declarations will be still included
    // f.e if we only include `openssl/*` headers
    // and in some declaration (X), there will be reference to some declaration (Y) from `stdio.h` header
    // this declaration (Y) will be also included (not referenced declarations will not be included)
    // otherwise there will be code, which could not be compiled
    // String is a wildcard pattern

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

public interface ForeignJvmCInterfaceTarget : ForeignCInterfaceTarget {
    //embeddedLibrariesPath=foreignLibs/*os* - should it be configurable?
    //embeddedLibrariesHashing=true
    //embedDynamicLibraries
    //embedStaticLibraries
    //embedLinkPaths - TODO?
}

public interface ForeignAndroidCInterfaceTarget : ForeignCInterfaceTarget {
    //embeddedLibrariesPath=foreignLibs/*os* - should it be configurable?
    //embeddedLibrariesHashing=true
    //embedDynamicLibraries
    //embedStaticLibraries
    //embedLinkPaths - TODO?
}

public interface ForeignNativeCInterfaceTarget : ForeignCInterfaceTarget {
    //embedStaticLibraries
    //embedLinkPaths
}

public interface ForeignWasmCInterfaceTarget : ForeignCInterfaceTarget {
    //embedStaticLibraries
    //embedLinkPaths
}
