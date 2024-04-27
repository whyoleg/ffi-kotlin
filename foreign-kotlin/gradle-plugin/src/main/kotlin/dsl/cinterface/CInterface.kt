package dev.whyoleg.foreign.gradle.plugin.dsl.cinterface

import dev.whyoleg.foreign.gradle.plugin.dsl.*
import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.jetbrains.kotlin.gradle.plugin.*

public interface ForeignCInterface : ForeignBaseCInterface, Named {
    // main by default
    public val sourceSetTree: Property<KotlinSourceSetTree>

    // false by default
    public val publicApi: Property<Boolean>

    // SOMETHING by default
    public val requiresOptIn: Property<String?>

    // TODO: may be improve granularity later
    // `foreign.{interface.name}` by default
    public val packageName: Property<String>

    // true by default
    // will cause adding foreign-runtime-c to dependencies
    // if `publicApi` = true -> as `api` dependency
    // if `publicApi` = false -> as `implementation` dependency
    public val autoRuntimeDependencies: Property<Boolean>

    public val platforms: NamedDomainObjectContainer<out ForeignPlatformCInterface>

    public fun jvm(configure: ForeignJvmCInterface.() -> Unit = {})
    public fun android(configure: ForeignAndroidCInterface.() -> Unit = {})
    public fun native(configure: ForeignNativeCInterface.() -> Unit = {})

//    public fun js(configure: ForeignWasmCInterface.() -> Unit) {}
//    public fun wasmJs(configure: ForeignWasmCInterface.() -> Unit) {}
}

public interface ForeignPlatformCInterface : ForeignBaseCInterface, Named {
    public val foreignPlatform: ForeignPlatform
    public val targets: NamedDomainObjectContainer<out ForeignTargetCInterface>
}

public interface ForeignTargetCInterface : ForeignBaseCInterface, Named {
    public val foreignTarget: ForeignTarget
}

public interface ForeignBaseCInterface {
    public val headerDirectories: ListProperty<Directory>
    public val libraryDirectories: ListProperty<Directory>
    public val libraryLinkageNames: ListProperty<String>

    //public val linkerArgs: ListProperty<String>

    public val initialHeaders: ListProperty<String>

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
