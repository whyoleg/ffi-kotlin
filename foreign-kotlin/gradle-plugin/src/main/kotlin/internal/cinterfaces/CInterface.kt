package dev.whyoleg.foreign.gradle.plugin.internal.cinterfaces

import dev.whyoleg.foreign.gradle.plugin.dsl.*
import dev.whyoleg.foreign.gradle.plugin.dsl.cinterface.*
import dev.whyoleg.foreign.gradle.plugin.internal.*
import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.model.*
import org.gradle.api.provider.*
import org.jetbrains.kotlin.gradle.plugin.*

internal class DefaultCInterface(
    private val objects: ObjectFactory,
    private val interfaceName: String
) : DefaultBaseCInterface(objects), ForeignCInterface {
    override fun getName(): String = interfaceName

    override val sourceSetTree: Property<KotlinSourceSetTree> =
        objects.property(KotlinSourceSetTree::class.java).convention(KotlinSourceSetTree.main)

    override val publicApi: Property<Boolean> =
        objects.property(Boolean::class.java).convention(false)

    override val requiresOptIn: Property<String?> =
        objects.property(String::class.java).convention("dev.whyoleg.foreign.ForeignFunctionInterface")

    override val autoRuntimeDependencies: Property<Boolean> =
        objects.property(Boolean::class.java).convention(true)

    override val packageName: Property<String> =
        objects.property(String::class.java).convention("foreign.$name")

    override val platforms: NamedDomainObjectContainer<DefaultPlatformCInterface> =
        objects.domainObjectContainer(DefaultPlatformCInterface::class.java)

    override fun jvm(configure: ForeignJvmCInterface.() -> Unit) {
        (platform(ForeignPlatform.Jvm) as ForeignJvmCInterface).configure()
    }

    override fun android(configure: ForeignAndroidCInterface.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun native(configure: ForeignNativeCInterface.() -> Unit) {
        //TODO("Not yet implemented")
    }

    private fun platform(platform: ForeignPlatform): ForeignPlatformCInterface {
        return platforms.getOrCreate(platform.name) {
            when (platform) {
                ForeignPlatform.Jvm     -> DefaultJvmCInterface(objects)
                ForeignPlatform.Android -> TODO()
                ForeignPlatform.Native  -> TODO()
                ForeignPlatform.Js      -> TODO()
                ForeignPlatform.WasmJs  -> TODO()
            }.withAllFrom(this)
        }
    }
}

internal abstract class DefaultBaseCInterface(
    objects: ObjectFactory,
) : ForeignBaseCInterface {
    final override val headerDirectories: ListProperty<Directory> = objects.listProperty(Directory::class.java)
    final override val libraryDirectories: ListProperty<Directory> = objects.listProperty(Directory::class.java)
    final override val libraryLinkageNames: ListProperty<String> = objects.listProperty(String::class.java)
    final override val initialHeaders: ListProperty<String> = objects.listProperty(String::class.java)

    final override val includedHeaderPatterns: ListProperty<String> = objects.listProperty(String::class.java)
    final override val excludedHeaderPatterns: ListProperty<String> = objects.listProperty(String::class.java)
    final override fun includeHeaders(vararg patterns: String): Unit = includedHeaderPatterns.addAll(*patterns)
    final override fun excludeHeaders(vararg patterns: String): Unit = excludedHeaderPatterns.addAll(*patterns)

    final override val includedVariablePatterns: ListProperty<String> = objects.listProperty(String::class.java)
    final override val excludedVariablePatterns: ListProperty<String> = objects.listProperty(String::class.java)
    final override fun includeVariables(vararg patterns: String): Unit = includedVariablePatterns.addAll(*patterns)
    final override fun excludeVariables(vararg patterns: String): Unit = excludedVariablePatterns.addAll(*patterns)

    final override val includedEnumPatterns: ListProperty<String> = objects.listProperty(String::class.java)
    final override val excludedEnumPatterns: ListProperty<String> = objects.listProperty(String::class.java)
    final override fun includeEnums(vararg patterns: String): Unit = includedEnumPatterns.addAll(*patterns)
    final override fun excludeEnums(vararg patterns: String): Unit = excludedEnumPatterns.addAll(*patterns)

    final override val includedTypedefPatterns: ListProperty<String> = objects.listProperty(String::class.java)
    final override val excludedTypedefPatterns: ListProperty<String> = objects.listProperty(String::class.java)
    final override fun includeTypedefs(vararg patterns: String): Unit = includedTypedefPatterns.addAll(*patterns)
    final override fun excludeTypedefs(vararg patterns: String): Unit = excludedTypedefPatterns.addAll(*patterns)

    final override val includedRecordPatterns: ListProperty<String> = objects.listProperty(String::class.java)
    final override val excludedRecordPatterns: ListProperty<String> = objects.listProperty(String::class.java)
    final override fun includeRecords(vararg patterns: String): Unit = includedRecordPatterns.addAll(*patterns)
    final override fun excludeRecords(vararg patterns: String): Unit = excludedRecordPatterns.addAll(*patterns)

    final override val includedFunctionPatterns: ListProperty<String> = objects.listProperty(String::class.java)
    final override val excludedFunctionPatterns: ListProperty<String> = objects.listProperty(String::class.java)
    final override fun includeFunctions(vararg patterns: String): Unit = includedFunctionPatterns.addAll(*patterns)
    final override fun excludeFunctions(vararg patterns: String): Unit = excludedFunctionPatterns.addAll(*patterns)
}

internal abstract class DefaultPlatformCInterface(
    objects: ObjectFactory,
) : DefaultBaseCInterface(objects), ForeignPlatformCInterface {
    final override fun getName(): String = foreignPlatform.name
}

internal abstract class DefaultTargetCInterface(
    objects: ObjectFactory
) : DefaultBaseCInterface(objects), ForeignTargetCInterface {
    final override fun getName(): String = foreignTarget.name
}

// TODO: setup conventions?
internal fun <T : DefaultBaseCInterface> T.withAllFrom(other: DefaultBaseCInterface): T {
    headerDirectories.addAll(other.headerDirectories)
    libraryDirectories.addAll(other.libraryDirectories)
    libraryLinkageNames.addAll(other.libraryLinkageNames)
    initialHeaders.addAll(other.initialHeaders)

    includedHeaderPatterns.addAll(other.includedHeaderPatterns)
    excludedHeaderPatterns.addAll(other.excludedHeaderPatterns)

    includedVariablePatterns.addAll(other.includedVariablePatterns)
    excludedVariablePatterns.addAll(other.excludedVariablePatterns)

    includedEnumPatterns.addAll(other.includedEnumPatterns)
    excludedEnumPatterns.addAll(other.excludedEnumPatterns)

    includedTypedefPatterns.addAll(other.includedTypedefPatterns)
    excludedTypedefPatterns.addAll(other.excludedTypedefPatterns)

    includedRecordPatterns.addAll(other.includedRecordPatterns)
    excludedRecordPatterns.addAll(other.excludedRecordPatterns)

    includedFunctionPatterns.addAll(other.includedFunctionPatterns)
    excludedFunctionPatterns.addAll(other.excludedFunctionPatterns)

    return this
}
