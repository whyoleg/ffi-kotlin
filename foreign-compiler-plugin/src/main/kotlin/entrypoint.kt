package dev.whyoleg.foreign.compiler

import dev.whyoleg.foreign.compiler.fir.*
import dev.whyoleg.foreign.compiler.ir.*
import org.jetbrains.kotlin.backend.common.extensions.*
import org.jetbrains.kotlin.compiler.plugin.*
import org.jetbrains.kotlin.config.*
import org.jetbrains.kotlin.fir.extensions.*

private const val PLUGIN_ID = "dev.whyoleg.foreign.compiler"

// compiler options
private val PLATFORM = CompilerConfigurationKey<String>("platform")

@OptIn(ExperimentalCompilerApi::class)
class ForeignCommandLineProcessor : CommandLineProcessor {
    private val options: Map<CliOption, CompilerConfiguration.(value: String) -> Unit> = buildMap {
        put(
            CliOption(
                optionName = "platform",
                valueDescription = "<platform>",
                description = "platform"
            )
        ) {
            put(PLATFORM, it)
        }
    }

    override val pluginId get() = PLUGIN_ID
    override val pluginOptions get() = options.keys

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        options[option]?.invoke(configuration, value) ?: throw CliOptionProcessingException("Unknown option: ${option.optionName}")
    }
}

@OptIn(ExperimentalCompilerApi::class)
class ForeignComponentRegistrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean get() = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        FirExtensionRegistrarAdapter.registerExtension(FirForeignExtensionRegistrar())
        IrGenerationExtension.registerExtension(ForeignIrGenerationExtension())
    }
}
